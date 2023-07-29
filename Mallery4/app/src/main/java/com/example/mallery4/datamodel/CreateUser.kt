package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class CreateUser (
    @SerializedName("userId") var userId:String,
    @SerializedName("password") var password:String,
    @SerializedName("phoneNumber") var phoneNumber:String,
    @SerializedName("username") var username:String,
)