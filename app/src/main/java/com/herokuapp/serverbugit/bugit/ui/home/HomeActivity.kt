package com.herokuapp.serverbugit.bugit.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolBar:Toolbar
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var navView:NavigationView
    private lateinit var navController: NavController
    private var homeActivityBinding:ActivityHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivityBinding = DataBindingUtil.setContentView(this,R.layout.activity_home)
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
}