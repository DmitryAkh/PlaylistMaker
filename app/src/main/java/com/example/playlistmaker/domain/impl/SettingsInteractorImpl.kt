package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactors.SettingsInteractor
import com.example.playlistmaker.domain.repositories.SettingsRepository


class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override fun provideIsNightMode(): Boolean = repository.provideIsNightMode()

    override fun switchTheme(darkThemeEnabled: Boolean) = repository.switchTheme(darkThemeEnabled)


    override fun setTheme(darkThemeEnabled: Boolean) = repository.setTheme(darkThemeEnabled)

}