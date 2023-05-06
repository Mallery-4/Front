package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class AllAlbumResponse (
    @SerializedName("state") var state:Int,
    @SerializedName("result") var result:String,
    @SerializedName("userId") var userId:String,
    @SerializedName("albumCnt") var albumCnt:Int,
    @SerializedName("albums") var albums:List<AlbumInfo>
)