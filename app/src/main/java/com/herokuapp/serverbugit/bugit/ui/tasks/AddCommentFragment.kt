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
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.TaskRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentAddCommentBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddCommentFragment : Fragment() {
    private var fragmentAddCommentBinding:FragmentAddCommentBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskRepo: TaskRepo
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private var token = ""
    private var userId = ""
    private var taskId = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback:OnBackPressedCallback = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.add_comment_to_single_task)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAddCommentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_comment,container,false)
        return fragmentAddCommentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            taskRepo = TaskRepo(token)
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { uid->
                userId = uid
                sharedViewModel.taskId.observe(viewLifecycleOwner, Observer { tid->
                    taskId = tid.toString()
                    taskViewModel = ViewModelProvider(this,TaskViewModelFactory(taskRepo))[TaskViewModel::class.java]
                    viewModelInitialized.postValue(true)
                })
            })
        })

        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            taskViewModel.postCommentStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    Snackbar.make(fragmentAddCommentBinding!!.postComment,"Commented", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.add_comment_to_single_task)
                }
                else{
                    Snackbar.make(fragmentAddCommentBinding!!.postComment,"Could not post your comment",Snackbar.LENGTH_SHORT).show()
                }
            })
        })

        fragmentAddCommentBinding!!.postComment.setOnClickListener {
            val comment = fragmentAddCommentBinding!!.commentText.text.toString()
            if (comment.isNotEmpty()){
                fragmentAddCommentBinding!!.commentLayout.error = null
                fragmentAddCommentBinding!!.postComment.isEnabled = false
                viewModelInitialized.observe(viewLifecycleOwner, Observer {
                    taskViewModel.postComment(UUID.fromString(taskId), UUID.fromString(userId),comment,getDateTimeFormatted())
                })
            }
            else{
                fragmentAddCommentBinding!!.commentLayout.error = "Type a comment first"
            }
        }
    }

    private fun getDateTimeFormatted():String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAddCommentBinding = null
    }
}