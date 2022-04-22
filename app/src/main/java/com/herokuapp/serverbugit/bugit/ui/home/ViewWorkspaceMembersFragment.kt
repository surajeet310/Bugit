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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.api.models.workspaces.WorkspaceMembers
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.HomeRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentViewWorkspaceMembersBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import java.util.*


class ViewWorkspaceMembersFragment : Fragment() {
    private var fragmentViewWorkspaceMembersBinding:FragmentViewWorkspaceMembersBinding? = null
    private lateinit var homeRepo: HomeRepo
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var viewWorkspaceMemberListAdapter: ViewWorkspaceMemberListAdapter
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var workspaceId:String = ""
    private var token:String = ""
    private var userId:String = ""
    private var workspaceAdmin = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback:OnBackPressedCallback = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.view_workspace_members_to_single_workspace)
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentViewWorkspaceMembersBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_view_workspace_members,container,false)
        return fragmentViewWorkspaceMembersBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = fragmentViewWorkspaceMembersBinding!!.workspaceMemberRecyclerView
        layoutManager = LinearLayoutManager(this.context)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { uid->
                userId = uid
                homeRepo = HomeRepo(token)
                sharedViewModel.workspaceId.observe(viewLifecycleOwner, Observer { wid->
                    workspaceId = wid.toString()
                    sharedViewModel.workspace_admin.observe(viewLifecycleOwner, Observer { isAdmin->
                        workspaceAdmin = isAdmin
                        homeFragmentViewModel = ViewModelProvider(this,HomeFragmentViewModelFactory(homeRepo))[HomeFragmentViewModel::class.java]
                        viewWorkspaceMemberListAdapter = ViewWorkspaceMemberListAdapter(workspaceAdmin,workspaceId,userId, homeFragmentViewModel)
                        viewModelInitialized.postValue(true)
                    })
                })
            })
        })
        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            fetchList()
            homeFragmentViewModel.deleteWorkspaceMemberStatus.observe(viewLifecycleOwner, Observer { deleteStatus->
                if (deleteStatus){
                    Snackbar.make(fragmentViewWorkspaceMembersBinding!!.workspaceMemberRecyclerView,"Member Removed",Snackbar.LENGTH_SHORT).show()
                    fetchList()
                }
                else{
                    Snackbar.make(fragmentViewWorkspaceMembersBinding!!.workspaceMemberRecyclerView,"Failed to remove member",Snackbar.LENGTH_SHORT).show()
                }
            })
            homeFragmentViewModel.makeUserAdminStatus.observe(viewLifecycleOwner, Observer { adminStatus->
                if (adminStatus){
                    Snackbar.make(fragmentViewWorkspaceMembersBinding!!.workspaceMemberRecyclerView,"User is admin now",Snackbar.LENGTH_SHORT).show()
                    fetchList()
                }
                else{
                    Snackbar.make(fragmentViewWorkspaceMembersBinding!!.workspaceMemberRecyclerView,"Failed to make user admin",Snackbar.LENGTH_SHORT).show()
                }
            })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewWorkspaceMembersBinding = null
    }

    private fun fetchList(){
        homeFragmentViewModel.getAllWorkspaceMembers(UUID.fromString(workspaceId))
        homeFragmentViewModel.allWorkspaceMembersStatus.observe(viewLifecycleOwner, Observer { status->
            if (status){
                homeFragmentViewModel.allWorkspaceMembersData.observe(viewLifecycleOwner,
                    Observer { workspaceMemberList->
                        renderList(workspaceMemberList)
                    })
            }
            else{
                Snackbar.make(fragmentViewWorkspaceMembersBinding!!.workspaceMemberRecyclerView,"Failed to fetch data",Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun renderList(workspaceMemberList:List<WorkspaceMembers>?){
        viewWorkspaceMemberListAdapter.submitList(workspaceMemberList)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = viewWorkspaceMemberListAdapter
        fragmentViewWorkspaceMembersBinding!!.progressIndicator.visibility = View.GONE
    }
}