package com.jesvardo.app.utils

import android.app.Application
import android.content.SharedPreferences
import com.jesvardo.app.base.MyApplication
import javax.inject.Inject

class AppPreferences(var application: Application) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    companion object {
        fun newInstance(application: Application): AppPreferences {
            return AppPreferences(application)
        }
    }

    init {
        MyApplication.appComponent.inject(this)
    }

    fun writeSharedPreferencesString(key: String?, value: String?) {
        sharedPreferencesEditor.putString(key, value)
        sharedPreferencesEditor.apply()
    }

    fun writeSharedPreferencesInt(key: String?, value: Int) {
        sharedPreferencesEditor.putInt(key, value)
        sharedPreferencesEditor.apply()
    }

    fun writeSharedPreferencesBool(key: String?, value: Boolean?) {
        sharedPreferencesEditor.putBoolean(key, value!!)
        sharedPreferencesEditor.apply()
    }

    fun clearAllPrefData() {
        sharedPreferencesEditor.putString(AppConstants.PREF_USER_ID, "")
        sharedPreferencesEditor.putString(AppConstants.PREF_USER_FIRST_NAME, "")
        sharedPreferencesEditor.putString(AppConstants.PREF_USER_LAST_NAME, "")
        sharedPreferencesEditor.putString(AppConstants.PREF_USER_PROFILE_IMAGE, "")
        sharedPreferencesEditor.putString(AppConstants.PREF_USER_COMPANY_NAME, "")
        sharedPreferencesEditor.putString(AppConstants.PREF_USER_EMAIL, "")
        sharedPreferencesEditor.putString(AppConstants.PREF_USER_PHONE_NO, "")
        sharedPreferencesEditor.putString(AppConstants.PREF_USER_DESIGNATION, "")
        sharedPreferencesEditor.putString(AppConstants.PREF_USER_TOKEN, "")
        sharedPreferencesEditor.putString(AppConstants.PREF_DEVICE_TOKEN, "")

        sharedPreferencesEditor.apply()
    }

    fun getAppPrefString(key: String?): String? {
        return try {
            sharedPreferences.getString(key, "")
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    fun getAppPrefBoolean(key: String?): Boolean {
        return try {
            sharedPreferences.getBoolean(key, false)
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }

}