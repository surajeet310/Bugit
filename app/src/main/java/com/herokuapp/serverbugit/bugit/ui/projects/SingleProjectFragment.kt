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
import com.herokuapp.serverbugit.api.models.projects.Task
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.ProjectRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentSingleProjectBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.util.*

class SingleProjectFragment : Fragment() {
    private var fragmentSingleProjectBinding:FragmentSingleProjectBinding? = null
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var projectRepo: ProjectRepo
    private lateinit var singleProjectListAdapter: SingleProjectListAdapter
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
                findNavController().navigate(R.id.single_project_to_single_workspace)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSingleProjectBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_single_project,container,false)
        return fragmentSingleProjectBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSingleProjectBinding!!.progressIndicator.visibility = View.VISIBLE
        recyclerView = fragmentSingleProjectBinding!!.taskListRecyclerView
        layoutManager = LinearLayoutManager(this.context)
        singleProjectListAdapter = SingleProjectListAdapter(sharedViewModel)
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
                        viewModelInitialized.postValue(true)
                    })
                })
            })
        })
        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            projectViewModel.getSingleProject(UUID.fromString(projectId), UUID.fromString(userId))
            projectViewModel.singleProjectStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    projectViewModel.singleProjectData.observe(viewLifecycleOwner, Observer { singleProjectData->
                        fragmentSingleProjectBinding!!.progressIndicator.visibility = View.GONE
                        fragmentSingleProjectBinding!!.let { binding->
                            binding.nameProject.text = singleProjectData.project.name
                            binding.descProject.text = singleProjectData.project.desc
                            binding.techProject.text = singleProjectData.project.tech
                            binding.createdAt.text = "Created on "+singleProjectData.project.createdAt
                            binding.deadline.text = "Deadline by "+singleProjectData.project.deadline
                            if (singleProjectData.project.memberCount == 1){
                                binding.memberCount.text = singleProjectData.project.memberCount.toString() + " Member"
                            }
                            if (singleProjectData.project.memberCount != 1){
                                binding.memberCount.text = singleProjectData.project.memberCount.toString() + " Members"
                            }
                            if (singleProjectData.project.taskCount == 1){
                                binding.taskCount.text = singleProjectData.project.taskCount.toString() + " Task"
                            }
                            if (singleProjectData.project.taskCount != 1){
                                binding.taskCount.text = singleProjectData.project.taskCount.toString() + " Tasks"
                            }
                            if (singleProjectData.project.isAdmin){
                                binding.addMember.isEnabled = true
                                binding.addTaskBtn.isEnabled = true
                                binding.deleteProject.isEnabled = true
                            }
                            if (singleProjectData.tasks != null){
                                binding.noTasks.visibility = View.GONE
                                renderList(singleProjectData.tasks)
                            }
                            if (singleProjectData.tasks == null){
                                binding.noTasks.visibility = View.VISIBLE
                                renderList(singleProjectData.tasks)
                            }
                        }
                    })
                }
                else{
                    Snackbar.make(fragmentSingleProjectBinding!!.noTasks,"Failed to fetch data",Snackbar.LENGTH_SHORT).show()
                }
            })
        })

        fragmentSingleProjectBinding!!.deleteProject.setOnClickListener {
            viewModelInitialized.observe(viewLifecycleOwner, Observer {
                projectViewModel.deleteProject(UUID.fromString(projectId))
            })
        }

        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            projectViewModel.deleteProjectStatus.observe(viewLifecycleOwner, Observer { deleteStatus->
                if (deleteStatus){
                    Snackbar.make(fragmentSingleProjectBinding!!.noTasks,"Project Deleted",Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.single_project_to_single_workspace)
                }
                else{
                    Snackbar.make(fragmentSingleProjectBinding!!.noTasks,"Failed to delete project",Snackbar.LENGTH_SHORT).show()
                }
            })
        })

        fragmentSingleProjectBinding!!.let { binding->
            binding.viewMembers.setOnClickListener {
                findNavController().navigate(R.id.single_project_to_view_project_members)
            }
            binding.addMember.setOnClickListener {
                findNavController().navigate(R.id.single_project_to_add_project_member)
            }
            binding.addTaskBtn.setOnClickListener {
                findNavController().navigate(R.id.single_project_to_add_task)
            }
        }
    }

    private fun renderList(taskList:List<Task>?){
        singleProjectListAdapter.submitList(taskList)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = singleProjectListAdapter
        fragmentSingleProjectBinding!!.progressIndicator.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSingleProjectBinding = null
    }
}