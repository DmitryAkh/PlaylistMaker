package com.example.playlistmaker.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.interactors.SettingsInteractor

class SettingsViewModel(private val interactor: SettingsInteractor) : ViewModel() {

    private val isNightMode = MutableLiveData<Boolean>()

    fun observeIsNightMode(): LiveData<Boolean> = isNightMode

    fun switchTheme(checked: Boolean) = interactor.switchTheme(checked)

    fun syncNightModeData() {
        isNightMode.postValue(interactor.provideIsNightMode())
    }

}