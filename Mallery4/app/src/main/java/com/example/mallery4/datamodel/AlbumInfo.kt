package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class AlbumInfo (
    @SerializedName("albumId") var albumId:Long,
    @SerializedName("albumName") var albumName:String,
    @SerializedName("memberCnt") var memberCnt:Int,
    @SerializedName("members") var members: List<String>,
    @SerializedName("nicknames") var nicknames: List<String>
)