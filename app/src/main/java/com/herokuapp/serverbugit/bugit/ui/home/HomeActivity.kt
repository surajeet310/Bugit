package com.herokuapp.serverbugit.bugit.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.herokuapp.serverbugit.api.models.users.UserDetails
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.ActivityHomeBinding
import com.herokuapp.serverbugit.bugit.shared.CurrentUser
import com.herokuapp.serverbugit.bugit.shared.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolBar:Toolbar
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var navView:NavigationView
    private lateinit var navController: NavController
    private var homeActivityBinding:ActivityHomeBinding? = null
    private val sharedViewModel:SharedViewModel by viewModels()
    private lateinit var user: CurrentUser
    private lateinit var userDetails:UserDetails
    private var token:String = ""
    private var userId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivityBinding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        token = intent.getStringExtra("token").toString()
        userId = intent.getStringExtra("userId").toString()
        sharedViewModel.token.postValue(token)
        sharedViewModel.userId.postValue(userId)
        user = CurrentUser(token)
        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        homeActivityBinding?.let {
            drawerLayout = it.drawerLayout
            navView = it.navigationView
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home,R.id.projects,R.id.assigned_task,R.id.task_assigned,R.id.requests,R.id.account,R.id.about
            ), drawerLayout
        )
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupActionBarWithNavController(navController, drawerLayout)
        navView.setupWithNavController(navController)
        fetchUser(user,userId)
        user.getUserDetail().observe(this, Observer { details->
            userDetails = details
            findViewById<TextView>(R.id.user_name).text = userDetails.fname
        })

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    //Goto Home
                    it.setChecked(true)
                    true
                }
                R.id.projects -> {
                    //Goto Projects
                    it.setChecked(true)
                    true
                }
                R.id.task_assigned -> {
                    //Goto Tasks assigned
                    it.setChecked(true)
                    true
                }
                R.id.assigned_task -> {
                    //Goto Assigned tasks
                    it.setChecked(true)
                    true
                }
                R.id.requests -> {
                    //Goto Requests
                    it.setChecked(true)
                    true
                }
                R.id.account -> {
                    //Goto account
                    it.setChecked(true)
                    true
                }
                R.id.about -> {
                    //Goto about
                    it.setChecked(true)
                    true
                }
                else -> false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            drawerLayout.openDrawer(Gravity.LEFT)
            return true
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun fetchUser(user: CurrentUser,userId:String){
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            user.getUserDetails(userId)
        }
    }
}