package com.herokuapp.serverbugit.bugit.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.api.models.workspaces.Project
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.HomeRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentSingleWorkspaceBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.util.*


class SingleWorkspaceFragment : Fragment() {
    private var fragmentSingleWorkspaceBinding:FragmentSingleWorkspaceBinding? = null
    private lateinit var homeRepo: HomeRepo
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var singleWorkspaceAdapter: SingleWorkspaceAdapter
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private var workspaceId:String = ""
    private var token:String = ""
    private var userId:String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.single_workspace_to_home)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSingleWorkspaceBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_single_workspace,container,false)
        return fragmentSingleWorkspaceBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(this.context)
        recyclerView = fragmentSingleWorkspaceBinding!!.projectListRecyclerView
        fragmentSingleWorkspaceBinding!!.progressIndicator.visibility = View.VISIBLE
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { id->
                userId = id
                homeRepo = HomeRepo(token)
                homeFragmentViewModel = ViewModelProvider(this,HomeFragmentViewModelFactory(homeRepo))[HomeFragmentViewModel::class.java]
                sharedViewModel.workspaceId.observe(viewLifecycleOwner, Observer { wId->
                    workspaceId = wId.toString()
                    singleWorkspaceAdapter = SingleWorkspaceAdapter(sharedViewModel)
                    viewModelInitialized.postValue(true)
                })
            })
        })
        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            if (it){
                homeFragmentViewModel.getSingleWorkspace(UUID.fromString(workspaceId), UUID.fromString(userId))
                homeFragmentViewModel.singleWorkspaceResponseStatus.observe(viewLifecycleOwner,
                    Observer { status->
                        if (status == false){
                            Snackbar.make(fragmentSingleWorkspaceBinding!!.noProjects,"Failed to fetch data",Snackbar.LENGTH_SHORT).show()
                        }
                        else{
                            homeFragmentViewModel.singleWorkspaceResponseData.observe(viewLifecycleOwner,
                                Observer { singleWorkspaceData->
                                    fragmentSingleWorkspaceBinding!!.let { binding->
                                        binding.progressIndicator.visibility = View.GONE
                                        sharedViewModel.workspace_admin.postValue(singleWorkspaceData.singleWorkspace.isAdmin)
                                        binding.nameWorkspace.text = singleWorkspaceData.singleWorkspace.name
                                        binding.descWorkspace.text = singleWorkspaceData.singleWorkspace.desc
                                        binding.createdAt.text = "Created on " + singleWorkspaceData.singleWorkspace.createdAt
                                        if (singleWorkspaceData.singleWorkspace.isAdmin){
                                            binding.deleteWorkspace.isEnabled = true
                                            binding.addProjectBtn.isEnabled = true
                                            binding.addMember.isEnabled = true
                                        }
                                        if (singleWorkspaceData.singleWorkspace.memberCount == 1){
                                            binding.memberCount.text = singleWorkspaceData.singleWorkspace.memberCount.toString() + " Member"
                                        }
                                        if (singleWorkspaceData.singleWorkspace.memberCount != 1){
                                            binding.memberCount.text = singleWorkspaceData.singleWorkspace.memberCount.toString() + " Members"
                                        }
                                        if (singleWorkspaceData.singleWorkspace.projectCount == 1){
                                            binding.projectCount.text = singleWorkspaceData.singleWorkspace.projectCount.toString() + " Project"
                                        }
                                        if (singleWorkspaceData.singleWorkspace.projectCount != 1){
                                            binding.projectCount.text = singleWorkspaceData.singleWorkspace.projectCount.toString() + " Projects"
                                        }
                                    }
                                    if (singleWorkspaceData.projects == null){
                                        fragmentSingleWorkspaceBinding!!.progressIndicator.visibility = View.GONE
                                        fragmentSingleWorkspaceBinding!!.noProjects.visibility = View.VISIBLE
                                        renderList(singleWorkspaceData.projects)
                                    }
                                    else{
                                        fragmentSingleWorkspaceBinding!!.noProjects.visibility = View.GONE
                                        renderList(singleWorkspaceData.projects)
                                    }
                                })
                        }

                    })
                fragmentSingleWorkspaceBinding!!.let { binding->
                    binding.addProjectBtn.setOnClickListener { btn->
                        btn.findNavController().navigate(R.id.single_workspace_to_add_project)
                    }
                    binding.addMember.setOnClickListener { btn->
                        btn.findNavController().navigate(R.id.single_workspace_to_add_workspace_member)
                    }
                    binding.viewMembers.setOnClickListener { btn->
                        btn.findNavController().navigate(R.id.single_workspace_to_view_workspace_members)
                    }
                }
            }
        })
        fragmentSingleWorkspaceBinding!!.deleteWorkspace.setOnClickListener {
            viewModelInitialized.observe(viewLifecycleOwner, Observer {
                homeFragmentViewModel.deleteWorkspace(UUID.fromString(workspaceId))
            })
        }
        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            homeFragmentViewModel.deleteWorkspaceStatus.observe(viewLifecycleOwner,
                Observer { deleteStatus->
                    if (deleteStatus){
                        Snackbar.make(fragmentSingleWorkspaceBinding!!.noProjects,"Workspace Deleted",Snackbar.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.single_workspace_to_home)
                    }
                })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSingleWorkspaceBinding = null
    }

    private fun renderList(projectList:List<Project>?){
        singleWorkspaceAdapter.submitList(projectList)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = singleWorkspaceAdapter
        fragmentSingleWorkspaceBinding!!.progressIndicator.visibility = View.GONE
    }

}