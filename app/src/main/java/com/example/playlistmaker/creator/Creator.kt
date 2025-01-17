package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.settings.data.SettingRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.SearchInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl

const val SHARED_PREFERENCES = "shared_preferences"

object Creator {

    private lateinit var application: Application


    fun initApplication(application: Application) {
        Creator.application = application
    }

    fun provideSharedPreferences(): SharedPreferences =
        application.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)


    private fun provideSettingsRepository(): SettingsRepository =
        SettingRepositoryImpl(provideSharedPreferences())


    private fun providePlayerRepository(): PlayerRepository =
        PlayerRepositoryImpl(mediaPlayer = MediaPlayer())

    fun providePlayerInteractor(): PlayerInteractor =
        PlayerInteractorImpl(providePlayerRepository())


    fun provideSettingsInteractor(): SettingsInteractor =
        SettingsInteractorImpl(provideSettingsRepository())

    private fun provideSearchRepository(context: Context): SearchRepository =
        SearchRepositoryImpl(RetrofitClient(context), provideSharedPreferences())

    fun provideSearchInteractor(context: Context): SearchInteractor =
        SearchInteractorImpl(provideSearchRepository(context))
}