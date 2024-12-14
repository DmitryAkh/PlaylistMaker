package com.example.playlistmaker.domain.impl

import SettingsRepository
import com.example.playlistmaker.domain.api.SettingsInteractor


class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override fun provideIsNightMode(): Boolean {
        return repository.provideIsNightMode()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        repository.switchTheme(darkThemeEnabled)
    }

    override fun setTheme(darkThemeEnabled: Boolean) {
        repository.setTheme(darkThemeEnabled)
    }

}