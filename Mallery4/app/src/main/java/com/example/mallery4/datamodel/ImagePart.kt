package com.example.mallery4.datamodel

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class ImagePart (
    @SerializedName("images") var images: List<MultipartBody.Part>,
)