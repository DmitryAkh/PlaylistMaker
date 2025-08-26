package com.example.playlistmaker.presentation

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactors.PlaylistsInteractor
import com.example.playlistmaker.domain.interactors.SearchInteractor
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    interactor: PlaylistsInteractor,
    searchInteractor: SearchInteractor,
) : PlaylistInternalsViewModel(interactor, searchInteractor) {

    fun updatePlaylistData(playlistId: Int, name: String, description: String, coverPath: String) {
        viewModelScope.launch {
            interactor.updatePlaylistData(playlistId, name, description, coverPath)
        }
    }

}