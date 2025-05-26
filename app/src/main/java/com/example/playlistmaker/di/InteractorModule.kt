package com.example.playlistmaker.di

import com.example.playlistmaker.domain.db.FavTracksInteractor
import com.example.playlistmaker.domain.impl.FavTracksInteractorImpl
import com.example.playlistmaker.domain.interactors.PlayerInteractor
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.interactors.SearchInteractor
import com.example.playlistmaker.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.interactors.SettingsInteractor
import com.example.playlistmaker.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }
    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<FavTracksInteractor> {
        FavTracksInteractorImpl(get())
    }
}