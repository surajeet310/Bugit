package com.herokuapp.serverbugit.bugit.data

import androidx.lifecycle.MutableLiveData
import com.herokuapp.serverbugit.api.BugitClient
import com.herokuapp.serverbugit.api.models.users.UserDetails
import com.herokuapp.serverbugit.api.models.users.UserUpdateFname
import com.herokuapp.serverbugit.api.models.users.UserUpdateLname
import java.util.*

class AccountRepo(private val token:String) {
    private val authApi = BugitClient.getAuthApiInstance()

    private var userData = MutableLiveData<UserDetails>()
    private var userStatus = MutableLiveData<Boolean>()
    private var updateFnameStatus = MutableLiveData<Boolean>()
    private var updateLnameStatus = MutableLiveData<Boolean>()
    private var deleteUserStatus = MutableLiveData<Boolean>()

    fun getUserData() = userData
    fun getUserStatus() = userStatus

    fun getUpdateFnameStatus() = updateFnameStatus
    fun getUpdateLnameStatus() = updateLnameStatus

    fun getDeleteUserStatus() = deleteUserStatus

    suspend fun fetchUser(userId:UUID){
        BugitClient.authToken = token
        val userResponse = authApi.getUser(userId)
        if (userResponse.body() != null){
            userData.postValue(userResponse.body()!!.result!!)
            userStatus.postValue(true)
        }
        else{
            userStatus.postValue(false)
        }
    }

    suspend fun updateFname(user:UserUpdateFname){
        BugitClient.authToken = token
        val updateFnameResponse = authApi.changeFname(user)
        if (updateFnameResponse.body() != null){
            updateFnameStatus.postValue(true)
        }
        else{
            updateFnameStatus.postValue(false)
        }
    }

    suspend fun updateLname(user:UserUpdateLname){
        BugitClient.authToken = token
        val updateLnameResponse = authApi.changeLname(user)
        if (updateLnameResponse.body() != null){
            updateLnameStatus.postValue(true)
        }
        else{
            updateLnameStatus.postValue(false)
        }
    }

    suspend fun deleteUser(userId: UUID){
        BugitClient.authToken = token
        val deleteUserResponse = authApi.deleteUser(userId)
        if (deleteUserResponse.body() != null){
            deleteUserStatus.postValue(true)
        }
        else{
            deleteUserStatus.postValue(false)
        }
    }
}