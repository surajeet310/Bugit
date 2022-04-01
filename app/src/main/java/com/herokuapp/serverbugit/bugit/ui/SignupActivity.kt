package com.herokuapp.serverbugit.bugit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private var signUpActivityBinding:ActivitySignupBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
    }
}