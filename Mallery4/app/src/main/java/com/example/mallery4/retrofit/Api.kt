package com.example.mallery4.retrofit

import com.example.mallery4.datamodel.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    // delete는 body 없으나, 현재의 경우 body 필요해서 해당 http로 메서드 구현해야 오류 안남.
    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path="album/{albumId}",hasBody = true)
    fun deletealbum(
        @Path("albumId") albumId:Long,
        @Body DeleteUser: DeleteUser
    ): Call<DeleteAlbumResponse>

    // 그룹 이름 변경하기
    @Headers("Content-Type: application/json")
    @PUT("album/{albumId}")
    fun updateAlbumName(
        @Path(value="albumId") albumId: Long,
        @Body UpdateAlbumname: UpdateAlbumname
    ) : Call<CreateAlbumResponse>

    // 새글쓰기(post)
    //@Headers("Content-Type: application/json")
    @Multipart
    @POST("post/new")
    fun writeText(
        @PartMap data : HashMap<String, RequestBody>,
        @Part images: List<MultipartBody.Part?>
    ) : Call<PostWriteResponse>

    //해당 앨범의 전체 대표 post만 확인하기
    @Headers("Content-Type: application/json")
    @GET("post/{albumId}/list")
    fun getAllPostInfo(@Path("albumId") albumId:Long) : Call<getAllPostResponse>

    //해당 앨범의 세부 post 내용 확인하기
    @Headers("Content-Type: application/json")
    @GET("post/{albumId}/{postId}")
    fun getDetailPostInfo(
        @Path("albumId") albumId:Long,
        @Path("postId") postId:Long
    ) : Call<getDetailPostResponse>

    // 글쓰기 수정(put)
    @Headers("Content-Type: application/json")
    @Multipart
    @PUT("post/{albumId}/{postId}")
    fun updateText(
        @Path("albumId") albumId: Long,
        @Path("postId") postId:Long,
        @PartMap map : Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part?>,
    ) : Call<PutWriteResponse>

    // 글쓰기 삭제(delete)
    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path="post/{albumId}/{postId}")
    fun deleteText(
        @Path("albumId") albumId:Long,
        @Path("postId") postId:Long,
    ): Call<DeleteWriteResponse>

}