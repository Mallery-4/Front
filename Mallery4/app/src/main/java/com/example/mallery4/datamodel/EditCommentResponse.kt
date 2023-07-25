package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class EditCommentResponse (
    @SerializedName("result") var result: String,
    @SerializedName("writer") var writer:String,
    @SerializedName("content") var content:String,
    @SerializedName("date") var date:String
    )