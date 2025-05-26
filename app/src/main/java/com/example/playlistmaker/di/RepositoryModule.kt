package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module


val repositoryModule = module {
    factory<PlayerRepository> {
        PlayerRepositoryImpl(get(), get())
    }
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}