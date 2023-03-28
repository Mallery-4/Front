package com.example.mallery4.retrofit

import com.example.mallery4.datamodel.AlreadyInUserID
import com.example.mallery4.datamodel.DefaultResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    //회원가입 : 성공시,
    @FormUrlEncoded
    @POST("member/signup")
    fun createUser(
        @Field("userId") userId:String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("phoneNumber") phoneNumber:String
    ): Call<DefaultResponse>

    //회원가입 : ID 중복확인
    @FormUrlEncoded
    @GET("member") // 전체 사용자 ID END-POINT 확인하기
    fun alreadyInUserID(
        @Field("userId") userId:String
    ): Call<AlreadyInUserID>
}