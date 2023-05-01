package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class Logindata (
    @SerializedName("grantType") var grantType:String,
    @SerializedName("accessToken") var accessToken:String,
    @SerializedName("refreshToken") var refreshToken:String,
    @SerializedName("refreshTokenExpirationTime") var refreshTokenExpirationTime:Long
    )