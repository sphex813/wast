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
        dataStore.edit { preferences ->
            preferences[key] = value!!
        }
    }

    suspend fun <T> getValue(key: Preferences.Key<T>): T? {
        return dataStore.data.map { preferences ->
            preferences[key]
        }.first()
    }

    suspend fun addToList(key: Preferences.Key<String>, value: String, callback: (() -> Unit)? = null): MutableList<String> {
        val tempList: MutableList<String> = getValueAsMutableList(key)
        val valueIndex = tempList.indexOf(value)
        if (valueIndex != -1) {
            tempList.removeAt(valueIndex)
        }
        tempList.add(0, value)
        storeValue(key, tempList.joinToString(","))
        if (callback != null) {
            callback()
        }
        return tempList
    }

    suspend fun removeFromList(key: Preferences.Key<String>, value: String): MutableList<String> {
        val tempList: MutableList<String> = getValueAsMutableList(key)
        val valueIndex = tempList.indexOf(value)
        if (valueIndex != -1) {
            tempList.removeAt(valueIndex)
        }
        storeValue(key, tempList.joinToString(","))
        return tempList
    }

    suspend fun removeFromList(key: Preferences.Key<String>, index: Int): MutableList<String> {
        val tempList: MutableList<String> = getValueAsMutableList(key)
        tempList.removeAt(index)
        storeValue(key, tempList.joinToString(","))
        return tempList
    }


    suspend fun getValueAsMutableList(key: Preferences.Key<String>): MutableList<String> {
        val storedValue = getValue(key)
        if (storedValue.isNullOrEmpty()) {
            return mutableListOf()
        } else {
            return storedValue.split(",").toMutableList()
        }
    }
}