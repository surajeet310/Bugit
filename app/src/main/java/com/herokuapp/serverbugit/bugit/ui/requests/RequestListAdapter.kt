package com.herokuapp.serverbugit.bugit.ui.requests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.herokuapp.serverbugit.api.models.workspaces.Request
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.FragmentRequestListItemBinding
import com.herokuapp.serverbugit.bugit.ui.home.HomeFragmentViewModel

class RequestListAdapter(private val homeFragmentViewModel: HomeFragmentViewModel):ListAdapter<Request,RequestListAdapter.RequestViewHolder>(DiffUtilComp()) {
    private var fragmentRequestListItemBinding:FragmentRequestListItemBinding? = null

    class RequestViewHolder(view:View,private val homeFragmentViewModel: HomeFragmentViewModel,private val fragmentRequestListItemBinding:FragmentRequestListItemBinding):RecyclerView.ViewHolder(view){
        fun bind(item:Request){
            fragmentRequestListItemBinding.userName.text = item.requestee
            fragmentRequestListItemBinding.acceptBtn.setOnClickListener {
                homeFragmentViewModel.acceptWorkspaceRequest(item.reqId)
            }
            fragmentRequestListItemBinding.ignoreBtn.setOnClickListener {
                homeFragmentViewModel.denyRequest(item.reqId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        fragmentRequestListItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_list_item,parent,false)
        return RequestViewHolder(fragmentRequestListItemBinding!!.root,homeFragmentViewModel,fragmentRequestListItemBinding!!)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilComp:DiffUtil.ItemCallback<Request>(){
        override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem.reqId == newItem.reqId
        }

        override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem == newItem
        }
    }
}