package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.FavoritesTracksViewModel
import com.example.playlistmaker.library.ui.PlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PlayerViewModel(get())
    }
    viewModel {
        SettingsViewModel(get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        PlaylistsViewModel()
    }
    viewModel {
        FavoritesTracksViewModel()
    }
}