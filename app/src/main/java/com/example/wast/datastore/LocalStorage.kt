package com.example.wast.datastore

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LocalStorage() : KoinComponent {
    private val app: Application by inject()

    private val dataStore: DataStore<Preferences> by lazy {
        app.createDataStore(name = "settings")
    }

    suspend fun <T> storeValue(key: Preferences.Key<T>, value: T?) {
        if (value != null) {
            dataStore.edit { preferences ->
                preferences[key] = value
            }
        }
    }

    suspend fun <T> getValue(key: Preferences.Key<T>): T? {
        return dataStore.data.map { preferences ->
            preferences[key]
        }.first()
    }

}