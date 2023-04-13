package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("state") var state:Int,
    @SerializedName("result") var result:String,
    @SerializedName("massage") var message:String,
    @SerializedName("data") var Logindata: Logindata,
    @SerializedName("error") var error:List<String>,
    @SerializedName("userId") var userId:String,
    @SerializedName("username") var username:String,
    @SerializedName("phoneNumber") var phoneNumber:String
)