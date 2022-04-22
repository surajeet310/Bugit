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
import com.herokuapp.serverbugit.bugit.databinding.FragmentAddWorkspaceMemberBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.util.*


class AddWorkspaceMemberFragment : Fragment() {
    private var fragmentAddWorkspaceMemberBinding:FragmentAddWorkspaceMemberBinding? = null
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private lateinit var homeRepo: HomeRepo
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private var token:String = ""
    private var userId:String = ""
    private var workspaceId:String = ""
    private var viewModelInitialized = MutableLiveData<Boolean>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.add_workspace_member_to_single_workspace)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAddWorkspaceMemberBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_workspace_member,container,false)
        return fragmentAddWorkspaceMemberBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { uid->
                userId = uid
                homeRepo = HomeRepo(token)
                sharedViewModel.workspaceId.observe(viewLifecycleOwner, Observer { wid->
                    workspaceId = wid.toString()
                    homeFragmentViewModel = ViewModelProvider(this,HomeFragmentViewModelFactory(homeRepo))[HomeFragmentViewModel::class.java]
                    viewModelInitialized.postValue(true)
                })
            })
        })
        fragmentAddWorkspaceMemberBinding!!.requestMemberBtn.setOnClickListener {
            viewModelInitialized.observe(viewLifecycleOwner, Observer {
                val email = fragmentAddWorkspaceMemberBinding!!.requestEmailId.text.toString()
                if (email.isNotEmpty()){
                    fragmentAddWorkspaceMemberBinding!!.progressIndicator.visibility = View.VISIBLE
                    fragmentAddWorkspaceMemberBinding!!.requestEmailIdLayout.error = null
                    homeFragmentViewModel.makeWorkspaceMemberRequest(UUID.fromString(workspaceId),
                        UUID.fromString(userId),email)
                    fragmentAddWorkspaceMemberBinding!!.requestMemberBtn.isEnabled = false
                }
                else{
                    fragmentAddWorkspaceMemberBinding!!.requestEmailIdLayout.error = "Email cannot be empty"
                }
            })
        }

        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            homeFragmentViewModel.addWorkspaceMemberReqStatus.observe(viewLifecycleOwner, Observer {
                if (it){
                    Snackbar.make(fragmentAddWorkspaceMemberBinding!!.requestMemberBtn,"Request Sent to user",Snackbar.LENGTH_SHORT).show()
                    fragmentAddWorkspaceMemberBinding!!.progressIndicator.visibility = View.GONE
                    findNavController().navigate(R.id.add_workspace_member_to_single_workspace)
                }
                else{
                    Snackbar.make(fragmentAddWorkspaceMemberBinding!!.requestMemberBtn,"Request could not be sent",Snackbar.LENGTH_SHORT).show()
                    fragmentAddWorkspaceMemberBinding!!.requestMemberBtn.isEnabled = true
                    fragmentAddWorkspaceMemberBinding!!.progressIndicator.visibility = View.GONE
                }
            })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAddWorkspaceMemberBinding = null
    }
}