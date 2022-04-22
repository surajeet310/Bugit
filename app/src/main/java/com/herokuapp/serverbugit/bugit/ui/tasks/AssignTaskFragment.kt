package com.herokuapp.serverbugit.bugit.ui.tasks

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
import com.herokuapp.serverbugit.bugit.data.TaskRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentAssignTaskBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.util.*


class AssignTaskFragment : Fragment() {
    private var fragmentAssignTaskBinding:FragmentAssignTaskBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskRepo: TaskRepo
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var assignTaskListAdapter:AssignTaskListAdapter
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private var token = ""
    private var userId = ""
    private var projectId = ""
    private var taskId = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback:OnBackPressedCallback = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.assign_task_to_single_task)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAssignTaskBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_assign_task,container,false)
        return fragmentAssignTaskBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = fragmentAssignTaskBinding!!.assignTaskRecyclerView
        layoutManager = LinearLayoutManager(this.context)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            taskRepo = TaskRepo(token)
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { uid->
                userId = uid
                sharedViewModel.projectId.observe(viewLifecycleOwner, Observer { pid->
                    projectId = pid.toString()
                    sharedViewModel.taskId.observe(viewLifecycleOwner, Observer { tid->
                        taskId = tid.toString()
                        taskViewModel = ViewModelProvider(this,TaskViewModelFactory(taskRepo))[TaskViewModel::class.java]
                        assignTaskListAdapter = AssignTaskListAdapter(UUID.fromString(userId),UUID.fromString(taskId),
                            taskViewModel, fragmentAssignTaskBinding!!)
                        viewModelInitialized.postValue(true)
                    })
                })
            })
        })

        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            taskViewModel.getTaskMembersToAdd(UUID.fromString(projectId), UUID.fromString(taskId))
            taskViewModel.taskMembersToAddStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    taskViewModel.taskMembersToAddData.observe(viewLifecycleOwner, Observer { memberList->
                        renderList(memberList)
                    })
                }
                else{
                    Snackbar.make(fragmentAssignTaskBinding!!.noMembers,"Failed to fetch data",Snackbar.LENGTH_SHORT).show()
                }
            })

            taskViewModel.assignTaskStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    Snackbar.make(fragmentAssignTaskBinding!!.noMembers,"Task Assigned",Snackbar.LENGTH_SHORT).show()
                    taskViewModel.getTaskMembersToAdd(UUID.fromString(projectId), UUID.fromString(taskId))
                }
                else{
                    Snackbar.make(fragmentAssignTaskBinding!!.noMembers,"Failed to assign task",Snackbar.LENGTH_SHORT).show()
                }
            })
        })
    }

    private fun renderList(members:List<ProjectMembers>){
        assignTaskListAdapter.submitList(members)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = assignTaskListAdapter
        fragmentAssignTaskBinding!!.progressIndicator.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAssignTaskBinding = null
    }
}