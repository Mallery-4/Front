package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap

data class UpdatePosting (
    @SerializedName("albumId") var albumId:Long,
    @SerializedName("postLocation") var postLocation:String,
    @SerializedName("postDate") var postDate:String,
    @SerializedName("userId") var userId:String,
    @SerializedName("participants") var participants:List<String>,
    //var images: List<MultipartBody.Part?>,
)