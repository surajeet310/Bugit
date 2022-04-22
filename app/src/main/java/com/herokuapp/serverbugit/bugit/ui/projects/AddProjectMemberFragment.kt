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
import com.herokuapp.serverbugit.api.models.workspaces.WorkspaceMembers
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.ProjectRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentAddProjectMemberBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.util.*

class AddProjectMemberFragment : Fragment() {
    private var fragmentAddProjectMemberBinding:FragmentAddProjectMemberBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var projectRepo: ProjectRepo
    private lateinit var addProjectMemberListAdapter: AddProjectMemberListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private var token = ""
    private var userId = ""
    private var workspaceId = ""
    private var projectId = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback:OnBackPressedCallback = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.add_project_member_to_single_project)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAddProjectMemberBinding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_add_project_member,container,false)
        return fragmentAddProjectMemberBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = fragmentAddProjectMemberBinding!!.addProjectMemberRecyclerView
        layoutManager = LinearLayoutManager(this.context)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            projectRepo = ProjectRepo(token)
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { uid->
                userId = uid
                sharedViewModel.workspaceId.observe(viewLifecycleOwner, Observer { wid->
                    workspaceId = wid.toString()
                    sharedViewModel.projectId.observe(viewLifecycleOwner, Observer { pid->
                        projectId = pid.toString()
                        projectViewModel = ViewModelProvider(this,ProjectViewModelFactory(projectRepo))[ProjectViewModel::class.java]
                        addProjectMemberListAdapter = AddProjectMemberListAdapter(UUID.fromString(projectId),projectViewModel)
                        viewModelInitialized.postValue(true)
                    })
                })
            })
        })

        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            projectViewModel.getProjectMembersToAdd(UUID.fromString(workspaceId), UUID.fromString(projectId))
            projectViewModel.projectMembersToAddStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    projectViewModel.projectMembersToAddData.observe(viewLifecycleOwner, Observer { members->
                        renderList(members)
                    })
                }
                else{
                    Snackbar.make(fragmentAddProjectMemberBinding!!.addProjectMemberRecyclerView,"Failed to fetch data",Snackbar.LENGTH_SHORT).show()
                }
            })

            projectViewModel.addProjectMemberStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    projectViewModel.getProjectMembersToAdd(UUID.fromString(workspaceId), UUID.fromString(projectId))
                    Snackbar.make(fragmentAddProjectMemberBinding!!.addProjectMemberRecyclerView,"User Added",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    Snackbar.make(fragmentAddProjectMemberBinding!!.addProjectMemberRecyclerView,"Failed to add user",Snackbar.LENGTH_SHORT).show()
                }
            })
        })
    }

    private fun renderList(memberList:List<WorkspaceMembers>){
        addProjectMemberListAdapter.submitList(memberList)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = addProjectMemberListAdapter
        fragmentAddProjectMemberBinding!!.progressIndicator.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAddProjectMemberBinding = null
    }
}