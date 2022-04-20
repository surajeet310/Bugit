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
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.TaskRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentAddTaskBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddTaskFragment : Fragment() {
    private var fragmentAddTaskBinding:FragmentAddTaskBinding? = null
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskRepo: TaskRepo
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private var token = ""
    private var userId = ""
    private var projectId = ""
    private var deadline = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback:OnBackPressedCallback = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.add_task_to_single_project)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAddTaskBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_task,container,false)
        return fragmentAddTaskBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            taskRepo = TaskRepo(token)
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { uid->
                userId = uid
                sharedViewModel.projectId.observe(viewLifecycleOwner, Observer { pid->
                    projectId = pid.toString()
                    taskViewModel = ViewModelProvider(this,TaskViewModelFactory(taskRepo))[TaskViewModel::class.java]
                    viewModelInitialized.postValue(true)
                })
            })
        })
        fragmentAddTaskBinding!!.addTaskBtn.setOnClickListener {
            val name = fragmentAddTaskBinding!!.taskName.text.toString()
            val desc = fragmentAddTaskBinding!!.taskDesc.text.toString()
            val tech = fragmentAddTaskBinding!!.taskTech.text.toString()
            if ((name.isNotEmpty()) && (desc.isNotEmpty()) && (tech.isNotEmpty()) && (deadline.isNotEmpty())){
                fragmentAddTaskBinding!!.let { binding->
                    binding.progressBar.visibility = View.VISIBLE
                    binding.taskNameLayout.error = null
                    binding.taskDescLayout.error = null
                    binding.taskTechLayout.error = null
                }
                viewModelInitialized.observe(viewLifecycleOwner, Observer {
                    taskViewModel.addTask(UUID.fromString(projectId),name,desc, UUID.fromString(userId),getDateTimeFormatted(),deadline, tech)
                })
            }
            else{
                if (name.isEmpty()){
                    fragmentAddTaskBinding!!.taskNameLayout.error = "Name cannot be empty"
                }
                if (desc.isEmpty()){
                    fragmentAddTaskBinding!!.taskDescLayout.error = "Description cannot be empty"
                }
                if (tech.isEmpty()){
                    fragmentAddTaskBinding!!.taskTechLayout.error = "Tech cannot be empty"
                }
                if (deadline.isEmpty()){
                    Snackbar.make(fragmentAddTaskBinding!!.addTaskBtn,"Deadline cannot be empty",Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        fragmentAddTaskBinding!!.selectDeadlineBtn.setOnClickListener {
            val today = MaterialDatePicker.todayInUtcMilliseconds()
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = today
            calendar[Calendar.MONTH] = Calendar.MONTH
            val currentMonth = calendar.timeInMillis
            val constraintsBuilder = CalendarConstraints.Builder()
                .setStart(currentMonth)
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select deadline")
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
            datePicker.show(parentFragmentManager,"Date Picker")

            datePicker.addOnPositiveButtonClickListener { selectedDate->
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                deadline = dateFormatter.format(Date(selectedDate))
            }
        }

        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            taskViewModel.addTaskStatus.observe(viewLifecycleOwner, Observer { status->
                fragmentAddTaskBinding!!.progressBar.visibility = View.GONE
                if (status){
                    Snackbar.make(fragmentAddTaskBinding!!.addTaskBtn,"Task Added",Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.add_task_to_single_project)
                }
                else{
                    Snackbar.make(fragmentAddTaskBinding!!.addTaskBtn,"Task could not be added",Snackbar.LENGTH_SHORT).show()
                }
            })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAddTaskBinding = null
    }

    private fun getDateTimeFormatted():String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }
}