package com.lavyshyk.countrycity.repository.sharedPreference

import android.content.SharedPreferences

interface SharedPrefRepository {

    fun getSharedPrefSubject(): SharedPreferences

    fun putStringSharedPref(key: String, string: String)

    fun getStringFromSharedPref(key: String): String

    fun putBooleanSharedPref(key: String, boolean: Boolean)

    fun getBooleanFromSharedPref(key: String): Boolean
}