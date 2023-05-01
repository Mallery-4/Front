package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class MypageResponse2 (
    @SerializedName("isSuccess") var isSuccess:Boolean,
    @SerializedName("code") var code:Int,
    @SerializedName("message") var username:String
)