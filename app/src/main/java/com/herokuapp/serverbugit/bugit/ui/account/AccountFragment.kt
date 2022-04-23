package com.herokuapp.serverbugit.bugit.ui.account

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.AccountRepo
import com.herokuapp.serverbugit.bugit.databinding.FragmentAccountBinding
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import com.herokuapp.serverbugit.bugit.ui.WelcomeActivity
import java.util.*

class AccountFragment : Fragment() {
    private var fragmentAccountBinding:FragmentAccountBinding? = null
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private lateinit var accountRepo: AccountRepo
    private lateinit var accountViewModel: AccountViewModel
    private var token = ""
    private var userId = ""
    private var viewModelInitialized = MutableLiveData<Boolean>()
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAccountBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_account,container,false)
        return fragmentAccountBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            accountRepo = AccountRepo(token)
            sharedViewModel.userId.observe(viewLifecycleOwner, Observer { uid->
                userId = uid
                accountViewModel = ViewModelProvider(this,AccountViewModelFactory(accountRepo))[AccountViewModel::class.java]
                viewModelInitialized.postValue(true)
            })
        })
        viewModelInitialized.observe(viewLifecycleOwner, Observer {
            accountViewModel.getUserDetails(UUID.fromString(userId))

            accountViewModel.userResponseStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    accountViewModel.userResponseData.observe(viewLifecycleOwner, Observer { user->
                        fragmentAccountBinding!!.fnameValue.setText(user.fname)
                        fragmentAccountBinding!!.lnameValue.setText(user.lname)
                        fragmentAccountBinding!!.emailValue.setText(user.email)
                        fragmentAccountBinding!!.profileImage.text = "${user.fname[0].uppercase()}${user.lname[0].uppercase()}"
                    })
                }
                else{
                    Snackbar.make(fragmentAccountBinding!!.deleteAcBtn,"Could not fetch data",Snackbar.LENGTH_SHORT).show()
                }
            })

            accountViewModel.changeFnameStatus.observe(viewLifecycleOwner, Observer { status->
                fragmentAccountBinding!!.saveBtn.visibility = View.GONE
                fragmentAccountBinding!!.progressBar.visibility = View.GONE
                if (status){
                    fragmentAccountBinding!!.fnameValue.isEnabled = false
                    Snackbar.make(fragmentAccountBinding!!.deleteAcBtn,"Update successful",Snackbar.LENGTH_SHORT).show()
                    accountViewModel.getUserDetails(UUID.fromString(userId))
                }
                else{
                    Snackbar.make(fragmentAccountBinding!!.deleteAcBtn,"Update failed",Snackbar.LENGTH_SHORT).show()
                }
            })

            accountViewModel.changeLnameStatus.observe(viewLifecycleOwner, Observer { status->
                fragmentAccountBinding!!.saveBtn.visibility = View.GONE
                fragmentAccountBinding!!.progressBar.visibility = View.GONE
                if (status){
                    fragmentAccountBinding!!.lnameValue.isEnabled = false
                    Snackbar.make(fragmentAccountBinding!!.deleteAcBtn,"Update successful",Snackbar.LENGTH_SHORT).show()
                    accountViewModel.getUserDetails(UUID.fromString(userId))
                }
                else{
                    Snackbar.make(fragmentAccountBinding!!.deleteAcBtn,"Update failed",Snackbar.LENGTH_SHORT).show()
                }
            })

            accountViewModel.deleteUserStatus.observe(viewLifecycleOwner, Observer { status->
                if (status){
                    requireActivity().let {
                        startActivity(Intent(this.context,WelcomeActivity::class.java))
                        it.finish()
                    }
                }
                else{
                    Snackbar.make(fragmentAccountBinding!!.deleteAcBtn,"Delete account failed",Snackbar.LENGTH_SHORT).show()
                }
            })
        })

        fragmentAccountBinding!!.editFnameBtn.setOnClickListener {
            fragmentAccountBinding!!.fnameValue.isEnabled = true
            fragmentAccountBinding!!.saveBtn.visibility = View.VISIBLE
        }

        fragmentAccountBinding!!.editLnameBtn.setOnClickListener {
            fragmentAccountBinding!!.lnameValue.isEnabled = true
            fragmentAccountBinding!!.saveBtn.visibility = View.VISIBLE
        }

        fragmentAccountBinding!!.saveBtn.setOnClickListener {
            if (fragmentAccountBinding!!.fnameValue.isEnabled){
                val fname = fragmentAccountBinding!!.fnameValue.text.toString()
                if (fname.isNotEmpty()){
                    fragmentAccountBinding!!.progressBar.visibility = View.VISIBLE
                    fragmentAccountBinding!!.saveBtn.visibility = View.GONE
                    fragmentAccountBinding!!.fnameLayout.error = null
                    viewModelInitialized.observe(viewLifecycleOwner, Observer {
                        accountViewModel.updateUserFname(UUID.fromString(userId),fname)
                    })
                }
                else{
                    fragmentAccountBinding!!.fnameLayout.error = "Cannot be empty"
                }
            }
            if (fragmentAccountBinding!!.lnameValue.isEnabled){
                val lname = fragmentAccountBinding!!.lnameValue.text.toString()
                if (lname.isNotEmpty()){
                    fragmentAccountBinding!!.progressBar.visibility = View.VISIBLE
                    fragmentAccountBinding!!.saveBtn.visibility = View.GONE
                    fragmentAccountBinding!!.lnameLayout.error = null
                    viewModelInitialized.observe(viewLifecycleOwner, Observer {
                        accountViewModel.updateUserLname(UUID.fromString(userId),lname)
                    })
                }
                else{
                    fragmentAccountBinding!!.lnameLayout.error = "Cannot be empty"
                }
            }
        }

        fragmentAccountBinding!!.deleteAcBtn.setOnClickListener {
            viewModelInitialized.observe(viewLifecycleOwner, Observer {
                accountViewModel.deleteUser(UUID.fromString(userId))
            })
        }

        fragmentAccountBinding!!.logoutBtn.setOnClickListener {
            sharedPrefs = requireActivity().getSharedPreferences("BugitCreds", AppCompatActivity.MODE_PRIVATE)
            editor = sharedPrefs.edit()
            editor.putString("Token",null).apply()
            requireActivity().let {
                startActivity(Intent(this.context,WelcomeActivity::class.java))
                it.finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAccountBinding = null
    }
}