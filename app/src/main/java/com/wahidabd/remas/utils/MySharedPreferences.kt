package com.wahidabd.remas.utils

import android.content.Context
import com.wahidabd.remas.data.models.User

class MySharedPreferences(context: Context) {

    companion object {
        private const val PREFS_NAME = "prefs_name"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
        private const val IMAGE = "image"

        private const val LOGIN = "login"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(data: User){
        val editor = prefs.edit()
        editor.putString(ID, data.id)
        editor.putString(NAME, data.name)
        editor.putString(EMAIL, data.email)
        editor.putString(TOKEN, data.token)
        editor.putString(IMAGE, data.image)
        editor.apply()
    }

    fun getUser(): User =
        User(
            id = prefs.getString(ID, "").toString(),
            name = prefs.getString(ID, "").toString(),
            email = prefs.getString(ID, "").toString(),
            token = prefs.getString(ID, "").toString(),
            image = prefs.getString(IMAGE, "").toString()
        )

    fun setLogin(value: Boolean){
        val editor = prefs.edit()
        editor.putBoolean(LOGIN, value)
        editor.apply()
    }

    fun getLogin(): Boolean =
        prefs.getBoolean(LOGIN, false)

    fun logout(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

}