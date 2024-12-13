package com.example.playlistmaker.domain.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.IS_NIGHT_MODE_KEY
import com.example.playlistmaker.domain.api.SettingsInteractor


class SettingsInteractorImpl : SettingsInteractor {

    private var darkTheme = false

    override fun switchTheme(darkThemeEnabled: Boolean, sharedPrefs: SharedPreferences) {
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