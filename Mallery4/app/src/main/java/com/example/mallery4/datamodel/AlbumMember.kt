package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class AlbumMember(
    @SerializedName("userId") var userId:String,
    @SerializedName("username") var username:String,
)