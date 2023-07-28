package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class DeleteWriteResponse (
    @SerializedName("result") var result:String,
    @SerializedName("postId") var postId:Long,
    @SerializedName("message") var message:String,
)