package com.herokuapp.serverbugit.bugit.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herokuapp.serverbugit.api.models.users.UserUpdateFname
import com.herokuapp.serverbugit.api.models.users.UserUpdateLname
import com.herokuapp.serverbugit.bugit.data.AccountRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AccountViewModel(private val accountRepo: AccountRepo):ViewModel() {

    var userResponseData = accountRepo.getUserData()
    var userResponseStatus = accountRepo.getUserStatus()

    var changeFnameStatus = accountRepo.getUpdateFnameStatus()

    var changeLnameStatus = accountRepo.getUpdateLnameStatus()

    var deleteUserStatus = accountRepo.getDeleteUserStatus()

    fun getUserDetails(userId:UUID){
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.fetchUser(userId)
        }
    }

    fun updateUserFname(userId: UUID,fname:String){
        val user = UserUpdateFname(userId, fname)
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.updateFname(user)
        }
    }

    fun updateUserLname(userId: UUID,lname:String){
        val user = UserUpdateLname(userId, lname)
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.updateLname(user)
        }
    }

    fun deleteUser(userId: UUID){
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.deleteUser(userId)
        }
    }
}