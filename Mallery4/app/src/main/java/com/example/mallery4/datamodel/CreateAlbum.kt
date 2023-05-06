package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class CreateAlbum (
    @SerializedName("albumName") var albumName:String,
    @SerializedName("userId") var userId:String
)