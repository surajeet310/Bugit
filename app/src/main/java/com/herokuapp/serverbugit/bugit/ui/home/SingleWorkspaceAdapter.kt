package com.herokuapp.serverbugit.bugit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.herokuapp.serverbugit.api.models.workspaces.Project
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.FragmentSingleWorkspaceListItemBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel

class SingleWorkspaceAdapter(private val sharedViewModel: SharedViewModel):ListAdapter<Project, SingleWorkspaceAdapter.SingleWorkspaceViewHolder>(DiffUtilComparator()) {
    private lateinit var fragmentSingleWorkspaceListItemBinding:FragmentSingleWorkspaceListItemBinding

    class SingleWorkspaceViewHolder(view:View,private val sharedViewModel: SharedViewModel,private val fragmentSingleWorkspaceListItemBinding:FragmentSingleWorkspaceListItemBinding):RecyclerView.ViewHolder(view){
        fun bind(item:Project){
            fragmentSingleWorkspaceListItemBinding.let {
                it.projectName.text = item.name
                if (item.memberCount == 1){
                    it.memberCount.text = item.memberCount.toString() + " Member"
                }
                if (item.memberCount != 1){
                    it.memberCount.text = item.memberCount.toString() + " Members"
                }
                if (item.taskCount == 1){
                    it.taskCount.text = item.taskCount.toString() + " Task"
                }
                if (item.taskCount != 1){
                    it.taskCount.text = item.taskCount.toString() + " Tasks"
                }
            }
            fragmentSingleWorkspaceListItemBinding.openProjectBtn.setOnClickListener {
                sharedViewModel.projectId.value = item.projectId
                it.findNavController().navigate(R.id.single_workspace_to_single_project)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleWorkspaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        fragmentSingleWorkspaceListItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_workspace_list_item,parent,false)
        return SingleWorkspaceViewHolder(fragmentSingleWorkspaceListItemBinding.root,sharedViewModel,fragmentSingleWorkspaceListItemBinding)
    }

    override fun onBindViewHolder(holder: SingleWorkspaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilComparator:DiffUtil.ItemCallback<Project>(){
        override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem.projectId == newItem.projectId
        }

        override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem == newItem
        }

    }
}