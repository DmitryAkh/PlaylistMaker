package com.example.playlistmaker.settings.domain

interface SettingsRepository {
    fun provideIsNightMode(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
    fun setTheme(darkThemeEnabled: Boolean)
}