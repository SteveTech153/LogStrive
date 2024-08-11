package com.example.logstrive.util

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREF_NAME = "UserSession"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    private const val KEY_USERNAME = "username"
    private const val KEY_USERID = "userid"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun login(context: Context, username: String, userid: Int) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USERNAME, username)
        editor.putInt(KEY_USERID, userid)
        editor.apply()
    }

    fun updateUsername(context: Context, username: String){
        val editor = getPreferences(context).edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    fun logout(context: Context) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.remove(KEY_USERNAME)
        editor.apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUsername(context: Context): String? {
        return getPreferences(context).getString(KEY_USERNAME, null)
    }
    fun getId(context: Context): Int {
        return getPreferences(context).getInt(KEY_USERID, 0)
    }
}
