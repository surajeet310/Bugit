package com.herokuapp.serverbugit.bugit.ui.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.api.models.projects.ProjectMembers
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.FragmentViewProjectMemberListItemBinding
import java.util.*

class ViewProjectMemberListAdapter(private val isAdmin:Boolean,private val projectId:UUID,private val userId:UUID,private val projectViewModel: ProjectViewModel):ListAdapter<ProjectMembers,ViewProjectMemberListAdapter.ViewProjectMemberViewHolder>(DiffUtilComp()) {
    private var fragmentViewProjectMemberListItemBinding:FragmentViewProjectMemberListItemBinding? = null

    class ViewProjectMemberViewHolder(view:View,private val isAdmin:Boolean,private val projectId:UUID,private val userId:UUID,private val projectViewModel: ProjectViewModel,private val fragmentViewProjectMemberListItemBinding:FragmentViewProjectMemberListItemBinding):RecyclerView.ViewHolder(view){
        fun bind(item:ProjectMembers,count:Int){
            if (isAdmin){
                fragmentViewProjectMemberListItemBinding.removeMember.isEnabled = true
                fragmentViewProjectMemberListItemBinding.makeAdminBtn.isEnabled = true
            }
            if (item.userId == userId){
                fragmentViewProjectMemberListItemBinding.userName.text = "You"
            }
            if (item.userId != userId){
                fragmentViewProjectMemberListItemBinding.userName.text = item.username
            }
            if (item.isAdmin){
                fragmentViewProjectMemberListItemBinding.adminText.visibility = View.VISIBLE
            }
            fragmentViewProjectMemberListItemBinding.makeAdminBtn.setOnClickListener {
                if (item.isAdmin){
                    Snackbar.make(it,"User is already admin",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    projectViewModel.makeProjectMemberAdmin(projectId, item.userId)
                }
            }
            fragmentViewProjectMemberListItemBinding.removeMember.setOnClickListener {
                if (count == 1){
                    Snackbar.make(it,"Cannot delete only project member",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    projectViewModel.removeProjectMember(projectId, item.userId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewProjectMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        fragmentViewProjectMemberListItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_project_member_list_item,parent,false)
        return ViewProjectMemberViewHolder(fragmentViewProjectMemberListItemBinding!!.root,isAdmin,projectId,userId,projectViewModel,fragmentViewProjectMemberListItemBinding!!)
    }

    override fun onBindViewHolder(holder: ViewProjectMemberViewHolder, position: Int) {
        holder.bind(getItem(position),itemCount)
    }

    class DiffUtilComp:DiffUtil.ItemCallback<ProjectMembers>(){
        override fun areItemsTheSame(oldItem: ProjectMembers, newItem: ProjectMembers): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: ProjectMembers, newItem: ProjectMembers): Boolean {
            return oldItem == newItem
        }
    }
}