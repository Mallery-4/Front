package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class getCommentInfoResponse(
    @SerializedName("result") val result: String,
    @SerializedName("count") val count: Int,
    @SerializedName("comments") val comments: List<CommentRes>
)


