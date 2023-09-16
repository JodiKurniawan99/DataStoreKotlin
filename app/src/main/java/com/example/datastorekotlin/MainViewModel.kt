package com.example.datastorekotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        //asLiveData function used for convert Flow to LiveData
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        //viewmodelscope is used for run couroutine in ViewModel
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}