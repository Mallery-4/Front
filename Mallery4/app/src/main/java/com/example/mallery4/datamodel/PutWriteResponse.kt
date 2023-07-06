package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class PutWriteResponse (
    @SerializedName("state") var state:Int,
    @SerializedName("result") var result:String,
    @SerializedName("postId") var postId:Long,
    @SerializedName("postDate") var postDate:String,
    @SerializedName("postLocation") var postLocation:String,
    @SerializedName("userId") var userId:String,
    @SerializedName("memberCnt") var memberCnt:Int,
    @SerializedName("updated") var updated:Boolean,
    @SerializedName("members") var members:List<String>,
    @SerializedName("nicknames") var nicknames:List<String>,
    @SerializedName("imagePaths") var imagePaths:List<String>
)
