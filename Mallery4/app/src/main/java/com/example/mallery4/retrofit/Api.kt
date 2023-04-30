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

    //마이페이지-자신의 정보 확인하기
    @Headers("Content-Type: application/json")
    @GET("member/{userId}")
    fun getUserInfo(@Path("userId") userId:String) : Call<MypageResponse>

    //마이페이지-자신의 정보 수정하기
    /*
    @Headers("Content-Type: application/json")
    @PUT("member/{userId}")
    fun modify(
        @Path(value="id",encoded=true) id:Int,
        @Body writeDTO: writeDTO) : Call<writeDTO>

     */
}