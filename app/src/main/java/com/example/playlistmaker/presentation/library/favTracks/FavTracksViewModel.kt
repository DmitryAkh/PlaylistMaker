package com.example.playlistmaker.presentation.library.favTracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.converters.TrackConverter
import com.example.playlistmaker.domain.db.FavTracksInteractor
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.launch

class FavTracksViewModel(
    private val interactor: FavTracksInteractor,
) : ViewModel() {

    private val favTracksLiveData = MutableLiveData<List<Track>>()

    fun loadFavTracks() {
        viewModelScope.launch {
            interactor
                .getFavTracks()
                .collect { favTracksDto ->
                    val favTracks = favTracksDto.map {
                        TrackConverter.map(it)
                    }
                    favTracksLiveData.postValue(favTracks)
                }
        }
    }

    fun putTrackForPlayer(track: Track) = interactor.putTrackForPlayer(track)

    fun observeFavTracks(): LiveData<List<Track>> = favTracksLiveData

}
