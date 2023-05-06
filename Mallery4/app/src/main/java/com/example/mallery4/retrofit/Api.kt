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
    @Headers("Content-Type: application/json")
    @PUT("member/{userId}")
    fun updateUserInfo(
        @Path(value="userId",encoded=true) userId: String,
        @Body UpdateUserInfo: UpdateUserInfo
    ) : Call<MypageResponse2>

    //그룹 앨범 생성하기 - 앨범생성
    @Headers("Content-Type: application/json")
    @POST("album/new")
    fun createAlbum(
        @Body
        CreateAlbum: CreateAlbum
    ): Call<CreateAlbumResponse>

    //그룹 앨범 생성하기 - 친구추가
    @Headers("Content-Type: application/json")
    @PUT("album/member/add")
    fun addFriend(
        @Body
        AddFriend: AddFriend
    ): Call<AddFriendResponse>

    //홈페이지-자신이 속한 전체 그룹 확인하기
    @Headers("Content-Type: application/json")
    @GET("album/member/{userId}")
    fun getAllAlbumInfo(@Path("userId") userId:String) : Call<AllAlbumResponse>

    // 그룹 삭제하기
    @Headers("Content-Type: application/json")
    @DELETE("album/{albumId}")
    fun deletealbum(@Path("albumId") albumId:Long): Call<DeleteAlbumResponse>

    // 그룹 이름 변경하기
    @Headers("Content-Type: application/json")
    @PUT("album/{albumId}")
    fun updateAlbumName(
        @Path(value="albumId") albumId: Long,
        @Body UpdateAlbumname: UpdateAlbumname
    ) : Call<CreateAlbumResponse>
}