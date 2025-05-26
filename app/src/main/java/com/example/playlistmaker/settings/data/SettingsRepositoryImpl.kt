package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.SettingsRepository


class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences) : SettingsRepository {

    private var darkTheme = false

    override fun provideIsNightMode(): Boolean = sharedPrefs.getBoolean(IS_NIGHT_MODE_KEY, false)

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

    companion object {
        const val IS_NIGHT_MODE_KEY = "key_for_night_mode"

    }

}