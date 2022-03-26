package com.herokuapp.serverbugit.api.services

import com.herokuapp.serverbugit.api.models.GlobalServerResponse
import com.herokuapp.serverbugit.api.models.users.UserRegister
import com.herokuapp.serverbugit.api.models.users.UserSignIn
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BugitApiServices{

    @POST("/open/register")
    suspend fun registerUser(
        @Body user:UserRegister
    ):Response<GlobalServerResponse>

    @POST("/open/login")
    suspend fun signInUser(
        @Body user:UserSignIn
    ):Response<GlobalServerResponse>

}