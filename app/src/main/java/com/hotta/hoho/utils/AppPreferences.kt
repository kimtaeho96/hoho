package com.hotta.hoho.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.LocaleList
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.*

object AppPreferences {
    private var mApplicationContext: Context? = null
    private const val NAME = "AOS_MOVIE"

    private val TAG = "!!@@" + AppPreferences::class.java.simpleName
    fun init(context: Context?) {
        mApplicationContext = context
    }

    private val preferences: SharedPreferences?
        get() {
            if (mApplicationContext == null) {
                Log.e(TAG, "error preferences not init")
                return null
            }
            return mApplicationContext!!.getSharedPreferences(NAME, Activity.MODE_PRIVATE)
        }

    private fun getString(key: String, defVal: String): String? {
        val pref = preferences
        if (pref == null) {
            Log.e(TAG, "error Preferences not init getString key : $key")
            return ""
        }
        return pref.getString(key, defVal)
    }

    private fun putBoolean(key: String, value: Boolean) {
        val pref = preferences
        if (pref == null) {
            Log.e(TAG, "error Preferences not init putString key : $key value : $value")
            return
        }
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun getBoolean(key: String, isDefault: Boolean): Boolean {
        val pref = preferences
        if (pref == null) {
            Log.e(TAG, "error Preferences not init getString key : $key")
            return false
        }
        return pref.getBoolean(key, isDefault)
    }

    fun getAutoLogin(): Boolean? {
        return getBoolean("Auto_Login", false)
    }

    fun setAutoLogin(boolean: Boolean) {
        var boolean = boolean;
        putBoolean("Auto_Login", boolean)
    }
}