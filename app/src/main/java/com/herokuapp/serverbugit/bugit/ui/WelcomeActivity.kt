package com.herokuapp.serverbugit.bugit.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.ActivityWelcomeBinding
import com.herokuapp.serverbugit.bugit.shared.CurrentUser
import com.herokuapp.serverbugit.bugit.ui.home.HomeActivity
import com.herokuapp.serverbugit.bugit.ui.login.LoginActivity
import com.herokuapp.serverbugit.bugit.ui.signup.SignupActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {
    private var welcomeActivityBinding:ActivityWelcomeBinding? = null
    private lateinit var sharedPrefs:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomeActivityBinding = DataBindingUtil.setContentView(this,R.layout.activity_welcome)
        sharedPrefs = getSharedPreferences("BugitCreds", MODE_PRIVATE)
        if (!sharedPrefs.contains("Token")){
            welcomeActivityBinding?.let {
                it.btnLayout.visibility = View.VISIBLE
                it.loginBtn.setOnClickListener {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                it.signUpBtn.setOnClickListener {
                    startActivity(Intent(this, SignupActivity::class.java))
                    finish()
                }
            }
        }
        else{
            welcomeActivityBinding!!.progressBar.visibility = View.VISIBLE
            val token = sharedPrefs.getString("Token","")
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("token",token)
            val user = CurrentUser(token!!)
            fetchUserId(user)
            user.getUserId().observe(this, Observer {
                intent.putExtra("userId",it)
                startActivity(intent)
                finish()
                welcomeActivityBinding!!.progressBar.visibility = View.GONE
            })
        }
    }

    private fun fetchUserId(user:CurrentUser){
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            user.getUser()
        }
    }
}