package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsViewModel(private val interactor: SettingsInteractor) : ViewModel() {

    val isNightMode = interactor.provideIsNightMode()

    fun switchTheme(checked: Boolean) = interactor.switchTheme(checked)

}