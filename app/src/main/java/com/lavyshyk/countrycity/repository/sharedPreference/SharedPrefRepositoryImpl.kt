package com.lavyshyk.countrycity.repository.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import com.lavyshyk.countrycity.APP_PREFERENCES

class SharedPrefRepositoryImpl(private val context: Context): SharedPrefRepository {

    private val mSharedPrefSubject: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    override fun getSharedPrefSubject(): SharedPreferences = mSharedPrefSubject

    override fun putStringSharedPref(key: String, string: String) {
        mSharedPrefSubject
            .edit()
            .putString(key, string)
            .apply()
    }

    override fun getStringFromSharedPref(key: String): String =
        mSharedPrefSubject.getString(key, "").toString()

    override fun putBooleanSharedPref(key: String, boolean: Boolean) {
        mSharedPrefSubject
            .edit()
            .putBoolean(key, boolean)
            .apply()
    }

    override fun getBooleanFromSharedPref(key: String): Boolean =
        mSharedPrefSubject.getBoolean(key, false)
}