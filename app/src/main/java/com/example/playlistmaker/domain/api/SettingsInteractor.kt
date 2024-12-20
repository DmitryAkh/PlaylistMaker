package com.example.playlistmaker.domain.api


interface SettingsInteractor {
    fun provideIsNightMode(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
    fun setTheme(darkThemeEnabled: Boolean)
}