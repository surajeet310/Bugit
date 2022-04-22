package com.herokuapp.serverbugit.bugit.ui.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.herokuapp.serverbugit.api.models.workspaces.WorkspaceMembers
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.FragmentAddProjectMemberListItemBinding
import com.herokuapp.serverbugit.bugit.databinding.FragmentAddProjectMemberBinding
import java.util.*

class AddProjectMemberListAdapter(private val fragmentAddProjectMemberBinding:FragmentAddProjectMemberBinding,private val projectId:UUID,private val userId:UUID,private val projectViewModel: ProjectViewModel):ListAdapter<WorkspaceMembers,AddProjectMemberListAdapter.AddProjectMemberViewHolder>(DiffUtilComp()) {
    private var fragmentAddProjectMemberListItemBinding:FragmentAddProjectMemberListItemBinding? = null

    class AddProjectMemberViewHolder(view: View,private val projectId:UUID,private val userId:UUID,private val projectViewModel: ProjectViewModel ,private val fragmentAddProjectMemberListItemBinding:FragmentAddProjectMemberListItemBinding):RecyclerView.ViewHolder(view){
        fun bind(item:WorkspaceMembers){
            fragmentAddProjectMemberListItemBinding.userName.text = item.username
            if (item.isAdmin){
                fragmentAddProjectMemberListItemBinding.addMemberBtn.isEnabled = true
            }
            fragmentAddProjectMemberListItemBinding.addMemberBtn.setOnClickListener {
                projectViewModel.addProjectMember(projectId, userId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProjectMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        fragmentAddProjectMemberListItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_project_member_list_item,parent,false)
        return AddProjectMemberViewHolder(fragmentAddProjectMemberListItemBinding!!.root,projectId,userId,projectViewModel,fragmentAddProjectMemberListItemBinding!!)
    }

    override fun onBindViewHolder(holder: AddProjectMemberViewHolder, position: Int) {
        val item = getItem(position)
        if (!item.isTaken){
            fragmentAddProjectMemberBinding.noMembers.visibility = View.GONE
            holder.bind(item)
        }
        else{
            fragmentAddProjectMemberBinding.noMembers.visibility = View.VISIBLE
            fragmentAddProjectMemberBinding.addProjectMemberRecyclerView.visibility = View.GONE
        }
    }

    class DiffUtilComp:DiffUtil.ItemCallback<WorkspaceMembers>(){
        override fun areItemsTheSame(oldItem: WorkspaceMembers, newItem: WorkspaceMembers): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: WorkspaceMembers, newItem: WorkspaceMembers): Boolean {
            return oldItem == newItem
        }
    }
}