package com.example.mallery4.retrofit

import com.example.mallery4.datamodel.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    //회원가입 : 성공시,
    @Headers("Content-Type: application/json")
    @POST("member/signup")
    fun createUser(
        @Body
        CreateUser: CreateUser
    ): Call<DefaultResponse>

    //회원가입 : ID 중복확인
    @Headers("Content-Type: application/json")
    @POST("member/idCheck")
    fun alreadyInUserID(
        @Body
        IdCheck : IdCheck
    ): Call<AlreadyInUserID>

    //로그인
    @Headers("Content-Type: application/json")
    @POST("member/login")
    fun userLogin(
        @Body
        LoginUser: LoginUser
    ): Call<LoginResponse>
}