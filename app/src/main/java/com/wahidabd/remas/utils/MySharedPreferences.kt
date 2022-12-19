package com.wahidabd.remas.utils

import android.content.Context
import com.wahidabd.remas.domain.models.User

class MySharedPreferences(context: Context) {

    companion object {
        private const val PREFS_NAME = "prefs_name"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
        private const val IMAGE = "image"
        private const val ROLE = "role"
        private const val LOGIN = "login"
        private const val ADDRESS = "address"
        private const val AGE = "age"
        private const val PHONE = "phone"
        private const val FCM = "fcm"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(data: User){
        val editor = prefs.edit()
        editor.putString(ID, data.id)
        editor.putString(NAME, data.name)
        editor.putString(EMAIL, data.email)
        editor.putString(IMAGE, data.image)
        editor.putInt(ROLE, data.role!!)
        editor.putString(ADDRESS, data.address)
        editor.putInt(AGE, data.age!!)
        editor.putString(PHONE, data.phone)
        editor.apply()
    }

    fun getUser(): User =
        User(
            id = prefs.getString(ID, "").toString(),
            name = prefs.getString(NAME, "").toString(),
            email = prefs.getString(EMAIL, "").toString(),
            image = prefs.getString(IMAGE, "").toString(),
            role = prefs.getInt(ROLE, 0),
            address = prefs.getString(ADDRESS, "").toString(),
            age = prefs.getInt(AGE, 0),
            phone = prefs.getString(PHONE, "").toString()
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