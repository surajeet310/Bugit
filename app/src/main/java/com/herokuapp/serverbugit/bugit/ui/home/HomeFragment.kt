package com.herokuapp.serverbugit.bugit.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.HomeRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentHomeBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel

class HomeFragment : Fragment() {
    private var fragmentHomeBinding:FragmentHomeBinding? = null
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private lateinit var layoutManager:LinearLayoutManager
    private lateinit var recyclerView:RecyclerView
    private lateinit var homeListAdapter: HomeListAdapter
    private lateinit var homeRepo: HomeRepo
    private var token:String = ""
    private var userId:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return fragmentHomeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(this.context)
        recyclerView = fragmentHomeBinding!!.homeRecyclerView
        homeListAdapter = HomeListAdapter()
        fragmentHomeBinding!!.progressIndicator.visibility = View.VISIBLE
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { id->
                userId = id
                homeRepo = HomeRepo(token,userId)
                homeFragmentViewModel = ViewModelProvider(this,HomeFragmentViewModelFactory(homeRepo))[HomeFragmentViewModel::class.java]
                viewModelInitialized.postValue(true)
            })
        })

        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            if (it == true){
                homeFragmentViewModel.getHome()
                homeFragmentViewModel.homeResponseStatus.observe(viewLifecycleOwner, Observer { status->
                    if (status == false){
                        Snackbar.make(fragmentHomeBinding!!.noWorkspaces,"Failed to fetch data",Snackbar.LENGTH_LONG).show()
                    }
                    else{
                        homeFragmentViewModel.homeResponseData.observe(viewLifecycleOwner, Observer { homeList->
                            if (homeList == null){
                                fragmentHomeBinding!!.progressIndicator.visibility = View.GONE
                                fragmentHomeBinding!!.noWorkspaces.visibility = View.VISIBLE
                            }
                            else{
                                homeListAdapter.submitList(homeList)
                                recyclerView.layoutManager = layoutManager
                                recyclerView.setHasFixedSize(true)
                                recyclerView.adapter = homeListAdapter
                                fragmentHomeBinding!!.progressIndicator.visibility = View.GONE
                            }
                        })
                    }
                })
            }
        })

        fragmentHomeBinding!!.addWorkspaceBtn.setOnClickListener {
            it.findNavController().navigate(R.id.home_to_add_workspace)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHomeBinding = null
    }

}