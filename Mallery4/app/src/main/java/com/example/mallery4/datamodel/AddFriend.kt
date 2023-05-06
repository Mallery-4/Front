package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class AddFriend (
    @SerializedName("albumId") var albumId:Long,
    @SerializedName("userId") var userId:String
)