package com.herokuapp.serverbugit.bugit.ui.requests

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.api.models.workspaces.Request
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.HomeRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentRequestBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import com.herokuapp.serverbugit.bugit.ui.home.HomeFragmentViewModel
import com.herokuapp.serverbugit.bugit.ui.home.HomeFragmentViewModelFactory
import com.herokuapp.serverbugit.bugit.ui.home.HomeListAdapter
import java.util.*


class RequestFragment : Fragment() {
    private var fragmentRequestBinding:FragmentRequestBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var requestListAdapter: RequestListAdapter
    private lateinit var homeRepo: HomeRepo
    private var token:String = ""
    private var userId:String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback:OnBackPressedCallback = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.request_to_home)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentRequestBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_request,container,false)
        return fragmentRequestBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = fragmentRequestBinding!!.requestRecyclerView
        layoutManager = LinearLayoutManager(this.context)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { uid->
                userId = uid
                homeRepo = HomeRepo(token)
                homeFragmentViewModel = ViewModelProvider(this,HomeFragmentViewModelFactory(homeRepo))[HomeFragmentViewModel::class.java]
                requestListAdapter = RequestListAdapter(homeFragmentViewModel)
                viewModelInitialized.postValue(true)
            })
        })

        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            homeFragmentViewModel.getRequests(UUID.fromString(userId))

            homeFragmentViewModel.requestStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    homeFragmentViewModel.requestData.observe(viewLifecycleOwner, Observer { requests->
                        if (requests == null){
                            fragmentRequestBinding!!.noRequests.visibility = View.VISIBLE
                            renderList(requests)
                        }
                        else{
                            fragmentRequestBinding!!.noRequests.visibility = View.GONE
                            renderList(requests)
                        }
                    })
                }
                else{
                    Snackbar.make(fragmentRequestBinding!!.noRequests,"Failed to fetch requests",Snackbar.LENGTH_SHORT).show()
                }
            })

            homeFragmentViewModel.addWorkspaceMemberStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    Snackbar.make(fragmentRequestBinding!!.noRequests,"Request accepted",Snackbar.LENGTH_SHORT).show()
                    homeFragmentViewModel.getRequests(UUID.fromString(userId))
                }
                else{
                    Snackbar.make(fragmentRequestBinding!!.noRequests,"Failed to accept request",Snackbar.LENGTH_SHORT).show()
                }
            })

            homeFragmentViewModel.denyRequestStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    Snackbar.make(fragmentRequestBinding!!.noRequests,"Request ignored",Snackbar.LENGTH_SHORT).show()
                    homeFragmentViewModel.getRequests(UUID.fromString(userId))
                }
                else{
                    Snackbar.make(fragmentRequestBinding!!.noRequests,"Failed to ignore request",Snackbar.LENGTH_SHORT).show()
                }
            })
        })
    }

    private fun renderList(requestList:List<Request>?){
        requestListAdapter.submitList(requestList)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = requestListAdapter
        fragmentRequestBinding!!.progressIndicator.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentRequestBinding = null
    }
}