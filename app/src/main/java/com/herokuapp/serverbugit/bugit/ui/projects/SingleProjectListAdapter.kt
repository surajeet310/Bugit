package com.herokuapp.serverbugit.bugit.ui.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.herokuapp.serverbugit.api.models.projects.Task
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import com.herokuapp.serverbugit.bugit.databinding.FragmentSingleProjectListItemBinding

class SingleProjectListAdapter(private val sharedViewModel: SharedViewModel):ListAdapter<Task, SingleProjectListAdapter.SingleProjectViewHolder>(DiffUtilComp()) {
    private var fragmentSingleProjectListItemBinding:FragmentSingleProjectListItemBinding? = null

    class SingleProjectViewHolder(view:View,private val sharedViewModel: SharedViewModel,private val fragmentSingleProjectListItemBinding:FragmentSingleProjectListItemBinding):RecyclerView.ViewHolder(view){
        fun bind(item:Task){
            fragmentSingleProjectListItemBinding.taskName.text = item.name
            fragmentSingleProjectListItemBinding.openTaskBtn.setOnClickListener {
                it.findNavController().navigate(R.id.single_project_to_single_task)
                sharedViewModel.taskId.postValue(item.taskId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleProjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        fragmentSingleProjectListItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_project_list_item,parent,false)
        return SingleProjectViewHolder(fragmentSingleProjectListItemBinding!!.root,sharedViewModel,fragmentSingleProjectListItemBinding!!)
    }

    override fun onBindViewHolder(holder: SingleProjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilComp:DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.taskId == newItem.taskId
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}