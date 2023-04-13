package com.example.mallery4.storage

import android.content.Context
import com.example.mallery4.datamodel.CreateUser
import com.google.gson.annotations.SerializedName


class SharedPrefManager private constructor(private var mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            var sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("userId", "") != ""
        }

    val user: CreateUser
        get() {
            var sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return CreateUser(
                sharedPreferences.getString("userId", "").toString(),
                sharedPreferences.getString("username", null).toString(),
                sharedPreferences.getString("password", null).toString(),
                sharedPreferences.getString("phoneNumber", null).toString()
            )
        }


    fun saveUser(user: CreateUser) {

        var sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()

        editor.putString("id", user.userId)
        editor.putString("username", user.username)
        editor.putString("password", user.password)
        editor.putString("phoneNumber", user.phoneNumber)
        editor.apply()

    }

    fun clear() {
        var sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}