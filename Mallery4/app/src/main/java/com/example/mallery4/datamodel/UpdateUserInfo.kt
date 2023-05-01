package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class UpdateUserInfo(
    @SerializedName("userId") var userId:String,
    @SerializedName("phoneNumber") var phoneNumber:String,
    @SerializedName("username") var username:String
)