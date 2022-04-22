package com.herokuapp.serverbugit.bugit.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.herokuapp.serverbugit.api.models.tasks.TaskComment
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.FragmentSingleTaskListItemBinding
import java.util.*

class SingleTaskListAdapter(private val userId:UUID):ListAdapter<TaskComment,SingleTaskListAdapter.SingleTaskViewHolder>(DiffUtilComp()) {
    private var fragmentSingleTaskListItemBinding:FragmentSingleTaskListItemBinding? = null

    class SingleTaskViewHolder(view:View,private val userId:UUID,private val fragmentSingleTaskListItemBinding:FragmentSingleTaskListItemBinding):RecyclerView.ViewHolder(view){
        fun bind(item:TaskComment){
            if (userId == item.userId){
                fragmentSingleTaskListItemBinding.userName.text = "You"
            }
            if (userId != item.userId){
                fragmentSingleTaskListItemBinding.userName.text = item.userName
            }
            fragmentSingleTaskListItemBinding.comment.text = item.comment
            fragmentSingleTaskListItemBinding.createdAt.text = item.createdAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleTaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        fragmentSingleTaskListItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_task_list_item,parent,false)
        return SingleTaskViewHolder(fragmentSingleTaskListItemBinding!!.root,userId,fragmentSingleTaskListItemBinding!!)
    }

    override fun onBindViewHolder(holder: SingleTaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilComp:DiffUtil.ItemCallback<TaskComment>(){
        override fun areItemsTheSame(oldItem: TaskComment, newItem: TaskComment): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: TaskComment, newItem: TaskComment): Boolean {
            return oldItem == newItem
        }
    }
}