package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class Comment (
    @SerializedName("postId") var postId: Long,
    @SerializedName("userId") var userId:String,
    @SerializedName("content") var comment_text:String,
)