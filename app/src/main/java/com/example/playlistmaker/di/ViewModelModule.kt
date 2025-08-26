package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.FavTracksViewModel
import com.example.playlistmaker.presentation.EditPlaylistViewModel
import com.example.playlistmaker.presentation.PlaylistInternalsViewModel
import com.example.playlistmaker.presentation.PlaylistsViewModel
import com.example.playlistmaker.presentation.PlayerViewModel
import com.example.playlistmaker.presentation.SearchViewModel
import com.example.playlistmaker.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PlayerViewModel(get(), get(), get())
    }
    viewModel {
        SettingsViewModel(get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        PlaylistsViewModel(get())
    }
    viewModel {
        FavTracksViewModel(get())
    }
    viewModel {
        PlaylistInternalsViewModel(get(), get())
    }

    viewModel {
        EditPlaylistViewModel(get(), get())
    }
}