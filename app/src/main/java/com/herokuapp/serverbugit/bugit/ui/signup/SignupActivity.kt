package com.herokuapp.serverbugit.bugit.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.herokuapp.serverbugit.bugit.R
import com.herokuapp.serverbugit.bugit.data.SignUpRepo
import com.herokuapp.serverbugit.bugit.databinding.ActivitySignupBinding
import com.herokuapp.serverbugit.bugit.ui.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private var signUpActivityBinding: ActivitySignupBinding? = null
    private var signUpViewModel: SignUpViewModel? = null
    private var signUpRepo = SignUpRepo()
    private lateinit var fullName: List<String>
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        signUpViewModel = ViewModelProvider(this,SignUpViewModelFactory(signUpRepo))[SignUpViewModel::class.java]
        signUpActivityBinding?.let {
            it.signUpViewModel = signUpViewModel
            it.lifecycleOwner = this
            it.signUpBtn.setOnClickListener { btn ->
                if((it.nameInput.text.toString() != "")&&(it.emailInput.text.toString() != "")&&
                    (it.pwdInput.text.toString() != "")&&(it.confirmPwdInput.text.toString() != "")&&
                    (it.nameInput.text.toString().split(" ").toList().size > 1 )){
                    fullName = it.nameInput.text.toString().split(" ").toMutableList()
                    email = it.emailInput.text.toString()
                    password = it.pwdInput.text.toString()
                    signUpViewModel!!.registerUser(fullName[0],fullName[1],email, password)
                    it.signUpBtn.isEnabled = false
                }
                else{
                    if(it.nameInput.text.toString() == ""){
                        it.nameInputLayout.error = "Name cannot be empty"
                    }

                    if (it.nameInput.text.toString().split(" ").toList().size <= 1 ){
                        it.nameInputLayout.error = "Enter both first and last name"
                    }

                    if(it.emailInput.text.toString() == ""){
                        it.emailInputLayout.error = "Email cannot be empty"
                    }

                    if(it.pwdInput.text.toString() == ""){
                        it.pwdInputLayout.error = "Password cannot be empty"
                    }

                    if(it.confirmPwdInput.text.toString() == ""){
                        it.confirmPwdInputLayout.error = "Confirm password cannot be empty"
                    }
                }
            }
        }
        signUpViewModel?.let {
            it.name.observe(this, Observer { nameStr->
                if (nameStr.isNotEmpty()){
                    signUpActivityBinding?.nameInputLayout?.error = null
                }
            })

            it.email.observe(this, Observer { emailStr->
                if (emailStr.isNotEmpty()){
                    signUpActivityBinding?.emailInputLayout?.error = null
                }
            })

            it.password.observe(this, Observer { pass->
                if (pass.isNotEmpty()){
                    signUpActivityBinding?.pwdInputLayout?.error = null
                }
                if ((pass.isNotEmpty())&&(pass.length < 8)){
                    signUpActivityBinding?.pwdInputLayout?.error = "Password too short"
                }
                else{
                    signUpActivityBinding?.pwdInputLayout?.error = null
                    password = pass
                }
            })

            it.confirmPassword.observe(this, Observer { confirmPass->
                if (confirmPass != password){
                    signUpActivityBinding?.confirmPwdInputLayout?.error = "Password do not match"
                }
                else{
                    signUpActivityBinding?.confirmPwdInputLayout?.error = null
                }
            })

            it.signUpResponse.observe(this, Observer { responseMsg->
                if (responseMsg == "error"){
                    Snackbar.make(signUpActivityBinding?.signUpBtn!!.rootView,"Error Occurred. Please try again.",Snackbar.LENGTH_SHORT).show()
                    signUpActivityBinding?.signUpBtn?.isEnabled = true
                }
                else{
                    if (responseMsg == "success"){
                        startActivity(Intent(this,LoginActivity::class.java))
                        finish()
                    }
                    else{
                        Snackbar.make(signUpActivityBinding?.signUpBtn!!.rootView,"User Exists",Snackbar.LENGTH_SHORT).show()
                        signUpActivityBinding?.signUpBtn?.isEnabled = true
                    }
                }
            })
        }
    }
}