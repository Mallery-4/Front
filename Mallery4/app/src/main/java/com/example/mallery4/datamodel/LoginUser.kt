package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class LoginUser(
    @SerializedName("userId") var userId:String,
    @SerializedName("password") var password:String,
)