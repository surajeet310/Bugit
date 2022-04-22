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
import com.herokuapp.serverbugit.api.models.tasks.TaskComment
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.TaskRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentSingleTaskBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.util.*


class SingleTaskFragment : Fragment() {
    private var fragmentSingleTaskBinding:FragmentSingleTaskBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskRepo: TaskRepo
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var singleTaskListAdapter: SingleTaskListAdapter
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private var token = ""
    private var userId = ""
    private var taskId = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback:OnBackPressedCallback = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.single_task_to_single_project)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSingleTaskBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_single_task,container,false)
        return fragmentSingleTaskBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSingleTaskBinding!!.progressIndicator.visibility = View.VISIBLE
        recyclerView = fragmentSingleTaskBinding!!.commentListRecyclerView
        layoutManager = LinearLayoutManager(this.context)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            taskRepo = TaskRepo(token)
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { uid->
                userId = uid
                singleTaskListAdapter = SingleTaskListAdapter(UUID.fromString(userId))
                sharedViewModel.taskId.observe(viewLifecycleOwner, Observer { tid->
                    taskId = tid.toString()
                    taskViewModel = ViewModelProvider(this,TaskViewModelFactory(taskRepo))[TaskViewModel::class.java]
                    viewModelInitialized.postValue(true)
                })
            })
        })
        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            taskViewModel.getSingleTask(UUID.fromString(taskId))
            taskViewModel.singleTaskStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    taskViewModel.singleTaskData.observe(viewLifecycleOwner, Observer { singleTask->
                        fragmentSingleTaskBinding!!.let { binding->
                            binding.nameTask.text = singleTask.task.name
                            binding.descTask.text = singleTask.task.desc
                            binding.techTask.text = singleTask.task.tech
                            binding.createdAt.text = "Created on " + singleTask.task.createdAt
                            binding.deadline.text = "Deadline by " + singleTask.task.deadline
                            if (singleTask.task.assignee == UUID.fromString(userId)){
                                binding.assigneeName.text = "Assignee : You"
                                binding.deleteTask.isEnabled = true
                                binding.assignTask.isEnabled = true
                            }
                            if (singleTask.task.assignee != UUID.fromString(userId)){
                                binding.assigneeName.text = "Assignee : " + singleTask.task.assigneeName
                            }
                            if (singleTask.task.assignedTo != UUID(0,0)){
                                if (singleTask.task.assignedTo == UUID.fromString(userId)){
                                    binding.assignedToName.text = "Assigned To : You"
                                }
                                if (singleTask.task.assignedTo != UUID.fromString(userId)){
                                    binding.assignedToName.text = "Assigned To : " + singleTask.task.assignedToName
                                }
                            }
                            else{
                                binding.assignedToName.text = "Assigned To : None"
                            }
                        }
                        if (singleTask.comments != null){
                            renderList(singleTask.comments)
                            fragmentSingleTaskBinding!!.noComments.visibility = View.GONE
                        }
                        else{
                            renderList(singleTask.comments)
                            fragmentSingleTaskBinding!!.noComments.visibility = View.VISIBLE
                        }

                    })
                }
                else{
                    Snackbar.make(fragmentSingleTaskBinding!!.noComments,"Failed to fetch data",Snackbar.LENGTH_SHORT).show()
                }
            })

            taskViewModel.deleteTaskStatus.observe(viewLifecycleOwner, Observer { deleteStatus->
                if (deleteStatus){
                    Snackbar.make(fragmentSingleTaskBinding!!.noComments,"Task Deleted",Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.single_task_to_single_project)
                }
                else{
                    Snackbar.make(fragmentSingleTaskBinding!!.noComments,"Task could not be deleted",Snackbar.LENGTH_SHORT).show()
                }
            })
        })

        fragmentSingleTaskBinding!!.assignTask.setOnClickListener {
            findNavController().navigate(R.id.single_task_to_assign_task)
        }

        fragmentSingleTaskBinding!!.addCommentBtn.setOnClickListener {
            findNavController().navigate(R.id.single_task_to_add_comment)
        }

        fragmentSingleTaskBinding!!.deleteTask.setOnClickListener {
            viewModelInitialized.observe(viewLifecycleOwner, Observer {
                taskViewModel.deleteTask(UUID.fromString(taskId))
            })
        }
    }

    private fun renderList(commentList:List<TaskComment>?){
        singleTaskListAdapter.submitList(commentList)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = singleTaskListAdapter
        fragmentSingleTaskBinding!!.progressIndicator.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSingleTaskBinding = null
    }
}