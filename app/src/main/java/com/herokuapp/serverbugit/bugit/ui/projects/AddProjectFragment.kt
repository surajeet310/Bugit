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
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.ProjectRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentAddProjectBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddProjectFragment : Fragment() {
    private var fragmentAddProjectBinding:FragmentAddProjectBinding? = null
    private lateinit var projectRepo: ProjectRepo
    private lateinit var projectViewModel: ProjectViewModel
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private var token:String = ""
    private var userId:String = ""
    private var workspaceId:String = ""
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private var deadline:String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback:OnBackPressedCallback = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.add_project_to_single_workspace)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAddProjectBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_project,container,false)
        return fragmentAddProjectBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            projectRepo = ProjectRepo(token)
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { uid->
                userId = uid
                sharedViewModel.workspaceId.observe(viewLifecycleOwner, Observer { wid->
                    workspaceId = wid.toString()
                    projectViewModel = ViewModelProvider(this,ProjectViewModelFactory(projectRepo))[ProjectViewModel::class.java]
                    viewModelInitialized.postValue(true)
                })
            })
        })

        fragmentAddProjectBinding!!.addProjectBtn.setOnClickListener {
            fragmentAddProjectBinding!!.progressBar.visibility = View.VISIBLE
            fragmentAddProjectBinding!!.addProjectBtn.isEnabled = false
            val name = fragmentAddProjectBinding!!.projectName.text.toString()
            val desc = fragmentAddProjectBinding!!.projectDesc.text.toString()
            val tech = fragmentAddProjectBinding!!.projectTech.text.toString()
            if (name.isNotEmpty() && desc.isNotEmpty() && tech.isNotEmpty() && deadline.isNotEmpty()){
                fragmentAddProjectBinding!!.projectNameLayout.error = null
                fragmentAddProjectBinding!!.projectDescLayout.error = null
                fragmentAddProjectBinding!!.projectTechLayout.error = null
                viewModelInitialized.observe(viewLifecycleOwner, Observer { vstatus->
                    projectViewModel.addProject(UUID.fromString(workspaceId), UUID.fromString(userId),name,desc,getDateTimeFormatted(),tech, deadline)
                    projectViewModel.addProjectResponseStatus.observe(viewLifecycleOwner, Observer { status->
                        if (status){
                            fragmentAddProjectBinding!!.progressBar.visibility = View.GONE
                            Snackbar.make(it,"Project Added",Snackbar.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.add_project_to_single_workspace)
                        }
                        else{
                            fragmentAddProjectBinding!!.progressBar.visibility = View.GONE
                            fragmentAddProjectBinding!!.addProjectBtn.isEnabled = true
                            Snackbar.make(it,"Some error occurred",Snackbar.LENGTH_SHORT).show()
                        }
                    })
                })
            }
            else{
                fragmentAddProjectBinding!!.progressBar.visibility = View.GONE
                fragmentAddProjectBinding!!.addProjectBtn.isEnabled = true
                if (name.isEmpty()){
                    fragmentAddProjectBinding!!.projectNameLayout.error = "Name cannot be empty"
                }
                if (desc.isEmpty()){
                    fragmentAddProjectBinding!!.projectDescLayout.error = "Description cannot be empty"
                }
                if (tech.isEmpty()){
                    fragmentAddProjectBinding!!.projectTechLayout.error = "Technology cannot be empty"
                }
                if (deadline.isEmpty()){
                    Snackbar.make(it,"Deadline cannot be empty",Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        fragmentAddProjectBinding!!.selectDeadlineBtn.setOnClickListener {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAddProjectBinding = null
    }

    private fun getDateTimeFormatted():String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }
}