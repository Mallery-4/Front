package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class AddFriendResponse (
    @SerializedName("state") var state:Int,
    @SerializedName("result") var result:String,
    @SerializedName("albumName") var albumName:String,
    @SerializedName("memberCnt") var memberCnt:Int,
    @SerializedName("members") var members:List<String>,
    @SerializedName("nicknames") var nicknames:List<String>,
)