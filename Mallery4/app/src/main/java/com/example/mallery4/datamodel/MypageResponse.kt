package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class MypageResponse (
    @SerializedName("result") var result:String,
    @SerializedName("userId") var userId:String,
    @SerializedName("username") var username:String,
    @SerializedName("phone_number") var phoneNumber:String
)