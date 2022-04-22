package com.herokuapp.serverbugit.bugit.ui.projects

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.api.models.projects.ProjectMembers
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.ProjectRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentViewProjectMembersBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.util.*


class ViewProjectMemberFragment : Fragment() {
    private var fragmentViewProjectMembersBinding:FragmentViewProjectMembersBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var projectRepo: ProjectRepo
    private lateinit var viewProjectMemberListAdapter: ViewProjectMemberListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private var token = ""
    private var userId = ""
    private var projectId = ""
    private var projectIsAdmin = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback:OnBackPressedCallback = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.view_project_members_to_single_project)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentViewProjectMembersBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_view_project_members,container,false)
        return fragmentViewProjectMembersBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewProjectMembersBinding!!.progressIndicator.visibility = View.VISIBLE
        recyclerView = fragmentViewProjectMembersBinding!!.projectMemberRecyclerView
        layoutManager = LinearLayoutManager(this.context)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            projectRepo = ProjectRepo(token)
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { uid->
                userId = uid
                sharedViewModel.projectId.observe(viewLifecycleOwner, Observer { pid->
                    projectId = pid.toString()
                    sharedViewModel.project_admin.observe(viewLifecycleOwner, Observer { isAdmin->
                        projectIsAdmin = isAdmin
                        projectViewModel = ViewModelProvider(this,ProjectViewModelFactory(projectRepo))[ProjectViewModel::class.java]
                        viewProjectMemberListAdapter = ViewProjectMemberListAdapter(projectIsAdmin,UUID.fromString(projectId),
                            UUID.fromString(userId),projectViewModel)
                        viewModelInitialized.postValue(true)
                    })
                })
            })
        })
        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            projectViewModel.getAllProjectMembers(UUID.fromString(projectId))
            projectViewModel.allProjectMembersStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    projectViewModel.allProjectMembersData.observe(viewLifecycleOwner, Observer { memberList->
                        renderList(memberList)
                    })
                }
                else{
                    Snackbar.make(fragmentViewProjectMembersBinding!!.projectMemberRecyclerView,"Failed to fetch data",Snackbar.LENGTH_SHORT).show()
                }
            })

            projectViewModel.makeProjectMemberAdminStatus.observe(viewLifecycleOwner, Observer { adminStatus->
                if (adminStatus){
                    projectViewModel.getAllProjectMembers(UUID.fromString(projectId))
                    Snackbar.make(fragmentViewProjectMembersBinding!!.projectMemberRecyclerView,"User is admin now",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    Snackbar.make(fragmentViewProjectMembersBinding!!.projectMemberRecyclerView,"Failed to make user admin",Snackbar.LENGTH_SHORT).show()
                }
            })

            projectViewModel.removeProjectMemberStatus.observe(viewLifecycleOwner, Observer { deleteStatus->
                if (deleteStatus){
                    projectViewModel.getAllProjectMembers(UUID.fromString(projectId))
                    Snackbar.make(fragmentViewProjectMembersBinding!!.projectMemberRecyclerView,"User is removed",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    Snackbar.make(fragmentViewProjectMembersBinding!!.projectMemberRecyclerView,"Failed to remove user",Snackbar.LENGTH_SHORT).show()
                }
            })
        })
    }

    private fun renderList(memberList:List<ProjectMembers>?){
        viewProjectMemberListAdapter.submitList(memberList)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = viewProjectMemberListAdapter
        fragmentViewProjectMembersBinding!!.progressIndicator.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewProjectMembersBinding = null
    }
}