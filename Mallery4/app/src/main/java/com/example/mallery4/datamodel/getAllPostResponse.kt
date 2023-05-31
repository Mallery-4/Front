package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class getAllPostResponse (
    @SerializedName("state") var state:Int,
    @SerializedName("result") var result:String,
    @SerializedName("albumId") var albumId:Long,
    @SerializedName("albumName") var albumName:String,
    @SerializedName("memberCnt") var memberCnt:Int,
    @SerializedName("posts") var posts: List<PostInfo>
)