package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class DeleteUser (
    @SerializedName("userId") var userId:String
)