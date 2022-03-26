package com.herokuapp.serverbugit.api

import com.herokuapp.serverbugit.api.models.UUIDAdapter
import com.herokuapp.serverbugit.api.services.BugitApiServices
import com.herokuapp.serverbugit.api.services.BugitAuthApiServices
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object BugitClient {

    private const val baseUrl = "https://bugit-server.herokuapp.com/"
    var authToken:String? = null

    private val authInterceptor = Interceptor{ chain ->
        var request = chain.request()
        authToken?.let {
            request = request.newBuilder()
                .header("Authorization","Token $it")
                .build()
        }
        chain.proceed(request)
    }

    private val okhttpBuilder = OkHttpClient.Builder()
        .readTimeout(5,TimeUnit.SECONDS)
        .connectTimeout(2,TimeUnit.SECONDS)

    private val moshi = Moshi.Builder()
        .add(UUIDAdapter())
        .build()

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))

    private val publicApi = retrofitBuilder
        .client(okhttpBuilder.build())
        .build()
        .create(BugitApiServices::class.java)

    private val authApi = retrofitBuilder
        .client(okhttpBuilder.addInterceptor(authInterceptor).build())
        .build()
        .create(BugitAuthApiServices::class.java)

    fun getPublicApiInstance():BugitApiServices = publicApi

    fun getAuthApiInstance():BugitAuthApiServices = authApi

}