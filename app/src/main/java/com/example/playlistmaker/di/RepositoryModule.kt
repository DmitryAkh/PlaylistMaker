package com.example.playlistmaker.di

import com.example.playlistmaker.data.db.impl.FavTracksRepositoryImpl
import com.example.playlistmaker.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.domain.repositories.PlayerRepository
import com.example.playlistmaker.data.impl.SearchRepositoryImpl
import com.example.playlistmaker.domain.repositories.SearchRepository
import com.example.playlistmaker.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.db.FavTracksRepository
import com.example.playlistmaker.domain.repositories.SettingsRepository
import org.koin.dsl.module


val repositoryModule = module {
    factory<PlayerRepository> {
        PlayerRepositoryImpl(get(), get())
    }
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get(), get())
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
    single<FavTracksRepository> {
        FavTracksRepositoryImpl(get(), get())
    }
}