package com.example.playlistmaker.presentation.settings

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.interactors.SettingsInteractor

class SettingsViewModel(private val interactor: SettingsInteractor) : ViewModel() {

    val isNightMode = interactor.provideIsNightMode()

    fun switchTheme(checked: Boolean) = interactor.switchTheme(checked)

}