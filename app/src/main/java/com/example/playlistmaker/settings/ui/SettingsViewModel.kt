package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsViewModel(private val interactor: SettingsInteractor) : ViewModel() {

    val isNightMode = interactor.provideIsNightMode()

    fun switchTheme(checked: Boolean) = interactor.switchTheme(checked)

    companion object {
        fun getViewModelFactory(
            interactor: SettingsInteractor,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(interactor)
                }
            }
    }

}