package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName

data class PostInfo (
    @SerializedName("postId") var postId:Long,
    @SerializedName("mainImage") var mainImage:String,
    @SerializedName("postDate") var postDate:String
)