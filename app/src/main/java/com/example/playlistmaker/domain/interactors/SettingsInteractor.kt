package com.example.playlistmaker.domain.interactors


interface SettingsInteractor {
    fun provideIsNightMode(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
    fun setTheme(darkThemeEnabled: Boolean)
}