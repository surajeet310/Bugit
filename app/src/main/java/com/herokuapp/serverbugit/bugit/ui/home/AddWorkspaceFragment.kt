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
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.HomeRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentAddWorkspaceBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddWorkspaceFragment : Fragment() {
    private var addWorkspaceFragmentBinding:FragmentAddWorkspaceBinding? = null
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private lateinit var homeRepo: HomeRepo
    private lateinit var homeViewModel:HomeFragmentViewModel
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private var token:String = ""
    private var userId:String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.add_workspace_to_home)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addWorkspaceFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_workspace,container,false)
        return addWorkspaceFragmentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { id->
                userId = id
                homeRepo = HomeRepo(token,userId)
                homeViewModel = ViewModelProvider(this,HomeFragmentViewModelFactory(homeRepo))[HomeFragmentViewModel::class.java]
                viewModelInitialized.postValue(true)
            })
        })
        addWorkspaceFragmentBinding!!.addWorkspaceBtn.setOnClickListener {
            addWorkspaceFragmentBinding!!.progressBar.visibility = View.VISIBLE
            val name = addWorkspaceFragmentBinding!!.workspaceNameInput.text.toString()
            val desc = addWorkspaceFragmentBinding!!.workspaceDescInput.text.toString()
            if ((name.isNotEmpty())&& (desc.isNotEmpty())){
                viewModelInitialized.observe(viewLifecycleOwner, Observer { status->
                    if (status == true){
                        homeViewModel.addWorkspace(userId,name,desc,getDateTimeFormatted())
                        addWorkspaceFragmentBinding!!.addWorkspaceBtn.isEnabled = false

                        homeViewModel.addWorkspaceResponseStatus.observe(viewLifecycleOwner,
                            Observer { responseStatus->
                                if (responseStatus == true){
                                    addWorkspaceFragmentBinding!!.progressBar.visibility = View.GONE
                                    Snackbar.make(it,"Workspace Added",Snackbar.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.add_workspace_to_home)
                                }
                                else{
                                    addWorkspaceFragmentBinding!!.let { binding->
                                        binding.addWorkspaceBtn.isEnabled = true
                                        binding.progressBar.visibility = View.GONE
                                    }
                                }
                            })
                    }
                })
            }
            else{
                if (name.isEmpty()){
                    addWorkspaceFragmentBinding!!.let { binding->
                        binding.workspaceNameLayout.error = "Name cannot be empty"
                        binding.progressBar.visibility = View.GONE
                        binding.addWorkspaceBtn.isEnabled = true
                    }
                }
                if (desc.isEmpty()){
                    addWorkspaceFragmentBinding!!.let { binding->
                        binding.workspaceNameLayout.error = "Description cannot be empty"
                        binding.progressBar.visibility = View.GONE
                        binding.addWorkspaceBtn.isEnabled = true
                    }
                }
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
        addWorkspaceFragmentBinding = null
    }
}