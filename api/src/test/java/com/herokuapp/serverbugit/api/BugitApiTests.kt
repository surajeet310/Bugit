package com.herokuapp.serverbugit.api

import com.herokuapp.serverbugit.api.models.users.UserRegister
import com.herokuapp.serverbugit.api.models.users.UserSignIn
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import kotlin.random.Random

class BugitApiTests{
    val api = BugitClient.getPublicApiInstance()

    @Test
    fun `Login User`(){
        val email = "john.snow@abc.com"
        val pwd = "random@1234"
        val user = UserSignIn(email,pwd)

        runBlocking {
            val res = api.signInUser(user)
            assertNotNull(res.body()?.result)
        }
    }

    @Test
    fun `Register User`(){
        val fname = "Bruce"
        val lname = "Wayne"
        val email = "bruce.wayne${Random.nextInt(99,999)}@test.com"
        val pwd = "pass${Random.nextInt(999,9999)}"
        val user = UserRegister(fname,lname,email, pwd)

        runBlocking {
            val res = api.registerUser(user)
            assertEquals("success",res.body()?.response)
        }
    }

}