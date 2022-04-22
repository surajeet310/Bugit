package com.herokuapp.serverbugit.bugit.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.herokuapp.serverbugit.api.models.projects.ProjectMembers
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.FragmentAssignTaskListItemBinding
import com.herokuapp.serverbugit.bugit.databinding.FragmentAssignTaskBinding
import java.util.*

class AssignTaskListAdapter(private val userId:UUID,private val taskId:UUID,private val taskViewModel: TaskViewModel,private val fragmentAssignTaskBinding: FragmentAssignTaskBinding):ListAdapter<ProjectMembers,AssignTaskListAdapter.AssignTaskViewHolder>(DiffUtilComp()) {
    private var fragmentAssignTaskListItemBinding:FragmentAssignTaskListItemBinding? = null

    class AssignTaskViewHolder(view: View,private val userId:UUID,private val taskId:UUID,private val taskViewModel: TaskViewModel,private val fragmentAssignTaskListItemBinding:FragmentAssignTaskListItemBinding):RecyclerView.ViewHolder(view){
        fun bind(item:ProjectMembers){
            if (item.userId == userId){
                fragmentAssignTaskListItemBinding.userName.text = "You"
            }
            if (item.userId != userId){
                fragmentAssignTaskListItemBinding.userName.text = item.username
            }
            fragmentAssignTaskListItemBinding.assignTaskBtn.setOnClickListener {
                taskViewModel.assignTask(taskId,item.userId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignTaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        fragmentAssignTaskListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_assign_task_list_item,parent,false)
        return AssignTaskViewHolder(fragmentAssignTaskListItemBinding!!.root,userId,taskId,taskViewModel,fragmentAssignTaskListItemBinding!!)
    }

    override fun onBindViewHolder(holder: AssignTaskViewHolder, position: Int) {
        val item = getItem(position)
        if (!item.isAssigned){
            fragmentAssignTaskBinding.noMembers.visibility = View.GONE
            holder.bind(item)
        }
        else{
            fragmentAssignTaskBinding.noMembers.visibility = View.VISIBLE
            fragmentAssignTaskBinding.assignTaskRecyclerView.visibility = View.GONE
        }
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