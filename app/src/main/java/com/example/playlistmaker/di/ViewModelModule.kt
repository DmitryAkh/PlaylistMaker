package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.library.favTracks.FavTracksViewModel
import com.example.playlistmaker.presentation.library.playlists.EditPlaylistViewModel
import com.example.playlistmaker.presentation.library.playlists.internals.PlaylistInternalsViewModel
import com.example.playlistmaker.presentation.library.playlists.PlaylistsViewModel
import com.example.playlistmaker.presentation.player.PlayerViewModel
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.settings.SettingsViewModel
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