package com.salesmotoris.library

import android.content.Context

class SalesMotorisPref(val context: Context){

    companion object {
        const val ACCESS_TOKEN = "data.source.prefs.access_token"
        const val ID = "data.source.prefs.id"
        const val USERNAME = "data.source.prefs.username"
        const val EMAIL = "data.source.prefs.email"
    }

    private val sharedPreference = context.getSharedPreferences("data.source.prefs", Context.MODE_PRIVATE)
    private val editPreference = sharedPreference.edit()

    var accessToken: String?
        get() = sharedPreference.getString(ACCESS_TOKEN, null)
        set(value) = editPreference.putString(ACCESS_TOKEN, value).apply()

    var id: String?
        get() = sharedPreference.getString(ID, null)
        set(value) = editPreference.putString(ID, value).apply()

    var username: String?
        get() = sharedPreference.getString(USERNAME, null)
        set(value) = editPreference.putString(USERNAME, value).apply()

    var email: String?
        get() = sharedPreference.getString(EMAIL, null)
        set(value) = editPreference.putString(EMAIL, value).apply()

    fun clearData(){
        editPreference.putString(ACCESS_TOKEN, null).apply()
        editPreference.putString(ID, null).apply()
        editPreference.putString(USERNAME, null).apply()
        editPreference.putString(EMAIL, null).apply()
    }
}