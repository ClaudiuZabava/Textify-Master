package com.example.textify.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiService {
    @POST("send")
    fun sendMessage(@HeaderMap headers: HashMap<String,String>, @Body messageBody: String): Call<String>

    @GET("receive")
    fun receiveMessage(@HeaderMap headers: HashMap<String,String>, @Body messageBody: String): Call<String>
}