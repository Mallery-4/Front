package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class DeleteAlbumResponse (
    @SerializedName("state") var state:Int,
    @SerializedName("result") var result:String,
    @SerializedName("albumId") var albumId:Long,
)