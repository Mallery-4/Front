package com.example.mallery4.retrofit

import android.util.Base64
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {

    private val AUTH = "Basic " + Base64.encodeToString("min01:min1234".toByteArray(), Base64.NO_WRAP)

    // server start 주소
    private const val BASE_URL = "http://172.30.1.45/"
    //private const val BASE_URL = "http://localhost:8080/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("Authorization",AUTH)
                .method(original.method(),original.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(Api::class.java)
    }
}