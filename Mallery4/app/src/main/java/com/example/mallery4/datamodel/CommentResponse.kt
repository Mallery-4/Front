package com.example.mallery4.datamodel
import com.google.gson.annotations.SerializedName

data class CommentResponse (
    @SerializedName("result") var result: String,
    @SerializedName("writer") var writer:String,
    @SerializedName("content") var comment_text:String,
    @SerializedName("date") var comment_date:String
)