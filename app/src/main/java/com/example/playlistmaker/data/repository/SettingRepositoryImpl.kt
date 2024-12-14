package com.example.playlistmaker.data.repository

import SettingsRepository
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val IS_NIGHT_MODE_KEY = "key_for_night_mode"


class SettingRepositoryImpl(private val sharedPrefs: SharedPreferences) : SettingsRepository {

    private var darkTheme = false

    override fun provideIsNightMode(): Boolean {
        return sharedPrefs.getBoolean(IS_NIGHT_MODE_KEY, false)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
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

    override fun setTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}