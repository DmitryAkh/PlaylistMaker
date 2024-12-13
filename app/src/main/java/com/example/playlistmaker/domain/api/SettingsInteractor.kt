package com.example.playlistmaker.domain.api

import android.content.SharedPreferences

interface SettingsInteractor {
    fun switchTheme(darkThemeEnabled: Boolean, sharedPrefs: SharedPreferences)
    fun setTheme(darkThemeEnabled: Boolean)
}