package com.herokuapp.serverbugit.bugit.ui.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.SignInRepo
import com.herokuapp.serverbugit.bugit.databinding.ActivityLoginBinding
import com.herokuapp.serverbugit.bugit.shared.CurrentUser
import com.herokuapp.serverbugit.bugit.ui.home.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private var loginActivityBinding:ActivityLoginBinding? = null
    private var loginViewModel:LoginViewModel? = null
    private val loginRepo = SignInRepo()
    private lateinit var sharedPrefs:SharedPreferences
    private lateinit var editor:SharedPreferences.Editor
    private var email:String = ""
    private var password:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        sharedPrefs = getSharedPreferences("BugitCreds", MODE_PRIVATE)
        editor = sharedPrefs.edit()
        loginViewModel = ViewModelProvider(this,LoginViewModelFactory(loginRepo))[LoginViewModel::class.java]
        loginActivityBinding?.let {
            it.signInViewModel = loginViewModel
            it.lifecycleOwner = this
            it.signInBtn.setOnClickListener { btn->
                if ((it.emailInput.text.toString().isNotEmpty())&& (it.pwdInput.text.toString().isNotEmpty())){
                    email = it.emailInput.text.toString()
                    password = it.pwdInput.text.toString()
                    loginViewModel!!.signInUser(email, password)
                    it.progressBar.visibility = View.VISIBLE
                    it.signInBtn.isEnabled = false
                }
                else{
                    if (it.emailInput.text.toString().isEmpty()){
                        it.emailInputLayout.error = "Email cannot be empty"
                    }
                    if (it.pwdInput.text.toString().isEmpty()){
                        it.pwdInputLayout.error = "Password cannot be empty"
                    }
                }
            }
        }

        loginViewModel?.let {
            it.email.observe(this, Observer {
                if (loginActivityBinding?.emailInput?.text.toString().isNotEmpty()){
                    loginActivityBinding!!.emailInputLayout.error = null
                }
            })

            it.password.observe(this, Observer {
                if (loginActivityBinding?.pwdInput?.text.toString().isNotEmpty()){
                    loginActivityBinding!!.pwdInputLayout.error = null
                }
            })

            it.signInResponseStatus.observe(this, Observer { status->
                if (status == "exists!"){
                    loginActivityBinding!!.signInBtn.isEnabled = true
                    loginActivityBinding!!.progressBar.visibility = View.GONE
                    Snackbar.make(loginActivityBinding?.signInBtn!!.rootView,"User doesn't exist",Snackbar.LENGTH_SHORT).show()
                }
                if (status == "wrong"){
                    loginActivityBinding?.let { bindingObj->
                        bindingObj.signInBtn.isEnabled = true
                        bindingObj.progressBar.visibility = View.GONE
                        bindingObj.emailInputLayout.error = "Invalid Credentials"
                        bindingObj.pwdInputLayout.error = "Invalid Credentials"
                    }
                }
                if (status == "error"){
                    loginActivityBinding!!.signInBtn.isEnabled = true
                    loginActivityBinding!!.progressBar.visibility = View.GONE
                    Snackbar.make(loginActivityBinding?.signInBtn!!.rootView,"Error occurred. Try again",Snackbar.LENGTH_SHORT).show()
                }
                if (status == "success"){
                    Snackbar.make(loginActivityBinding?.signInBtn!!.rootView,"Successful login",Snackbar.LENGTH_SHORT).show()
                    it.token.observe(this, Observer { tokenStr->
                        if (tokenStr != null){
                            editor.putString("Token",tokenStr).apply()
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.putExtra("token",tokenStr)
                            val user = CurrentUser(tokenStr)
                            fetchUserId(user)
                            user.getUserId().observe(this, Observer { id->
                                intent.putExtra("userId",id)
                                startActivity(intent)
                                loginActivityBinding!!.progressBar.visibility = View.GONE
                                finish()
                            })
                        }
                        else{
                            loginActivityBinding!!.signInBtn.isEnabled = true
                            loginActivityBinding!!.progressBar.visibility = View.GONE
                            Snackbar.make(loginActivityBinding?.signInBtn!!.rootView,"Error occurred. Try again",Snackbar.LENGTH_SHORT).show()
                        }
                    })
                }
            })
        }
    }
    private fun fetchUserId(user:CurrentUser){
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            user.getUser()
        }
    }
}