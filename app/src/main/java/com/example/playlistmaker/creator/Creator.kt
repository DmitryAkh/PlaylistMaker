package com.example.playlistmaker.creator

import SettingsRepository
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.repository.SearchRepositoryImpl
import com.example.playlistmaker.data.repository.SettingRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerUseCase
import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.api.SearchRepository
import com.example.playlistmaker.domain.api.SettingsInteractor
import com.example.playlistmaker.domain.impl.PlayerUseCaseImpl
import com.example.playlistmaker.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.impl.SettingsInteractorImpl

const val SHARED_PREFERENCES = "shared_preferences"

object Creator {

    private lateinit var application: Application


    fun initApplication(application: Application) {
        this.application = application
    }

    fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun provideSettingsRepository(): SettingsRepository {
        return SettingRepositoryImpl(provideSharedPreferences())
    }

    fun provideMediaPlayerUseCase(): PlayerUseCase {
        return PlayerUseCaseImpl(MediaPlayer())
    }


    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository())
    }

    fun provideSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(RetrofitClient(), provideSharedPreferences())
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(provideSearchRepository())
    }
}