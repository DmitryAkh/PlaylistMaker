package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val SHARED_PREFERENCES = "shared_preferences"
const val IS_NIGHT_MODE_KEY = "key_for_night_mode"

class App : Application() {

    var darkTheme = false
    lateinit var sharedPrefs: SharedPreferences


    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

        darkTheme = sharedPrefs.getBoolean(IS_NIGHT_MODE_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs.edit()
            .putBoolean(IS_NIGHT_MODE_KEY, darkTheme)
            .apply()
    }


}