package com.herokuapp.serverbugit.bugit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.api.models.workspaces.WorkspaceMembers
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.FragmentViewWorkspaceMemberListItemBinding
import java.util.*

class ViewWorkspaceMemberListAdapter(private val workspaceId:String,private val userId:String,private val homeFragmentViewModel: HomeFragmentViewModel):ListAdapter<WorkspaceMembers, ViewWorkspaceMemberListAdapter.ViewWorkspaceMembersViewHolder>(DiffUtilComp()) {
    private var fragmentViewWorkspaceMemberListItemBinding:FragmentViewWorkspaceMemberListItemBinding? = null

    class ViewWorkspaceMembersViewHolder(view:View,private val workspaceId:String,private val userId:String,private val homeFragmentViewModel: HomeFragmentViewModel,private val fragmentViewWorkspaceMemberListItemBinding:FragmentViewWorkspaceMemberListItemBinding):RecyclerView.ViewHolder(view){
        fun bind(item:WorkspaceMembers,count:Int){
            if (item.userId == UUID.fromString(userId)){
                fragmentViewWorkspaceMemberListItemBinding.userName.text = "You"
            }
            if (item.userId != UUID.fromString(userId)){
                fragmentViewWorkspaceMemberListItemBinding.userName.text = item.username
            }
            if (item.isAdmin){
                fragmentViewWorkspaceMemberListItemBinding.makeAdminBtn.isEnabled = true
                fragmentViewWorkspaceMemberListItemBinding.removeMember.isEnabled = true
                fragmentViewWorkspaceMemberListItemBinding.adminText.visibility = View.VISIBLE
            }

            fragmentViewWorkspaceMemberListItemBinding.removeMember.setOnClickListener {
                if (count == 1){
                    Snackbar.make(it,"Cannot delete only workspace member",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    homeFragmentViewModel.removeWorkspaceMember(UUID.fromString(workspaceId))
                }
            }
            fragmentViewWorkspaceMemberListItemBinding.makeAdminBtn.setOnClickListener {
                if (item.isAdmin){
                    Snackbar.make(it,"User is Admin already",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    homeFragmentViewModel.makeWorkspaceUserAdmin(UUID.fromString(workspaceId))
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewWorkspaceMembersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        fragmentViewWorkspaceMemberListItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_workspace_member_list_item,parent,false)
        return ViewWorkspaceMembersViewHolder(fragmentViewWorkspaceMemberListItemBinding!!.root,workspaceId,userId,homeFragmentViewModel,fragmentViewWorkspaceMemberListItemBinding!!)
    }

    override fun onBindViewHolder(holder: ViewWorkspaceMembersViewHolder, position: Int) {
        holder.bind(getItem(position),itemCount)
    }

    class DiffUtilComp:DiffUtil.ItemCallback<WorkspaceMembers>(){
        override fun areItemsTheSame(
            oldItem: WorkspaceMembers,
            newItem: WorkspaceMembers
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: WorkspaceMembers,
            newItem: WorkspaceMembers
        ): Boolean {
            return oldItem == newItem
        }
    }
}