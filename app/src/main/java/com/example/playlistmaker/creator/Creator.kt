package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.MediaPlayerUseCase
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SettingsInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.api.TracksUseCase
import com.example.playlistmaker.domain.impl.MediaPlayerUseCaseImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.TracksUseCaseImpl

const val SHARED_PREFERENCES = "shared_preferences"
const val IS_NIGHT_MODE_KEY = "key_for_night_mode"

object Creator {

    private lateinit var application: Application


    fun initApplication(application: Application) {
        this.application = application
    }

    fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun provideIsNightMode(): Boolean {
        return provideSharedPreferences().getBoolean(IS_NIGHT_MODE_KEY, false)
    }

    private fun provideTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitClient())
    }

    fun provideMediaPlayerUseCase(): MediaPlayerUseCase {
        return MediaPlayerUseCaseImpl(MediaPlayer())
    }


    fun provideTracksUseCase(): TracksUseCase {

        return TracksUseCaseImpl(provideTracksRepository())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl()
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(
            application.getSharedPreferences(
                SHARED_PREFERENCES,
                Context.MODE_PRIVATE
            )
        )
    }
}