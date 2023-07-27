package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class CommentRes(
    @SerializedName("commentId") val commentId: Long,
    @SerializedName("writer") val writer: String,
    @SerializedName("content") val content: String,
    @SerializedName("date") val date: String
)