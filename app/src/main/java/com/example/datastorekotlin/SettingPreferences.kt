package com.example.datastorekotlin

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


//Create instance datastore preferences
//this created using property delegation, which this mechanism
//for delegate task to another class.
//with this method, we dont need to know how created instance datastore in detail.
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    //this code used for store data to data store prefrences with name theme_setting
    private val THEME_KEY = booleanPreferencesKey("theme_setting")


    //This function used for get theme value from datastore
    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        //Volatile is a keyword used to ensure that the value of a variable is not cached.
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            //syncronized function used for only one thread can run this function.
            //Because in android app can be multi threaded proccess,So an instance could potentially be created on a different thread.
            //If there are 2 instances alive with different data in each instance, it will result in inconsistent data anomalies.
            //If there are 2 instances alive with different data in each instance, it will result in inconsistent data anomalies. That's why Singleton plays a crucial role here.
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}