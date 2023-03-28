package com.example.mallery4.retrofit

import com.example.mallery4.datamodel.DefaultResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("member/signup")
    fun createUser(
        @Field("userId") userId:String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("phoneNumber") phoneNumber:String
    ): Call<DefaultResponse>
}