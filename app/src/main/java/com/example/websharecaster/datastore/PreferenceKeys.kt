package com.example.websharecaster.datastore

import androidx.datastore.preferences.core.preferencesKey

object PreferenceKeys {
    val USER_NAME = preferencesKey<String>("user_name")
    val PASSWORD = preferencesKey<String>("password")
    val TOKEN = preferencesKey<String>("token")
}