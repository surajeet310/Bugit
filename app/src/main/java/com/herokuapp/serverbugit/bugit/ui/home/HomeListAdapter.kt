package com.herokuapp.serverbugit.bugit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.herokuapp.serverbugit.api.models.workspaces.Home
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.FragmentHomeListItemBinding

class HomeListAdapter:ListAdapter<Home,HomeListAdapter.HomeListViewHolder>(DiffUtilComparator()) {
    private lateinit var homeListItemBinding:FragmentHomeListItemBinding

    class HomeListViewHolder(view: View, private val listItemBinding: FragmentHomeListItemBinding):RecyclerView.ViewHolder(view){
        fun bind(item:Home){
            listItemBinding.let {
                it.workspaceName.text = item.name
                it.memberCount.text = item.memberCount.toString()
                it.projectCount.text = item.projectCount.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        homeListItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_list_item,parent,false)
        return HomeListViewHolder(homeListItemBinding.root,homeListItemBinding)
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilComparator:DiffUtil.ItemCallback<Home>(){
        override fun areItemsTheSame(oldItem: Home, newItem: Home): Boolean {
            return oldItem.workspaceId == newItem.workspaceId
        }

        override fun areContentsTheSame(oldItem: Home, newItem: Home): Boolean {
            return oldItem == newItem
        }
    }

}