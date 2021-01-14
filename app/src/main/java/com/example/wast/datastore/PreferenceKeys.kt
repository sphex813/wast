package com.example.wast.datastore

import androidx.datastore.preferences.core.preferencesKey

object PreferenceKeys {
    val USER_NAME = preferencesKey<String>("user_name")
    val PASSWORD = preferencesKey<String>("password")
    val TOKEN = preferencesKey<String>("token")
    val HISTORY = preferencesKey<String>("history")
    val WATCHED = preferencesKey<String>("watched")
}