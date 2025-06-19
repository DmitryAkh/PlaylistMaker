package com.example.playlistmaker.presentation.player


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.converters.DbConverter
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.domain.db.FavTracksInteractor
import com.example.playlistmaker.domain.entity.PlayerScreenState
import com.example.playlistmaker.domain.entity.PlayerState
import com.example.playlistmaker.domain.interactors.PlayerInteractor
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.interactors.PlaylistsInteractor
import com.example.playlistmaker.util.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favTracksInteractor: FavTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
) : ViewModel() {
    private val player = playerInteractor.getPlayer()
    private var timerJob: Job? = null
    private val screenState = MutableLiveData<PlayerScreenState>()
    private var currentState = PlayerState.DEFAULT
    private var currentTime = "00:00"
    private var currentIsFavorite = false
    private var currentPlaylists: List<Playlist> = emptyList()

    fun observeScreenState(): LiveData<PlayerScreenState> = screenState


    private fun updateState() {
        screenState.postValue(
            PlayerScreenState(
                playerState = currentState,
                time = currentTime,
                isFavorite = currentIsFavorite,
                playlists = currentPlaylists
            )
        )
    }

    fun preparePlayer() {
        playerInteractor.preparePlayer()
        playerInteractor.setOnCompletionListener {
            currentState = PlayerState.PAUSED
            currentTime = "00:00"
            timerJob?.cancel()
            updateState()
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        currentState = PlayerState.PLAYING
        startTimer()
        updateState()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        currentState = PlayerState.PAUSED
    }

    fun togglePlayer() {
        val state = playerInteractor.getState()

        if (state == PlayerState.PLAYING) {
            pausePlayer()
        } else if (state == PlayerState.PREPARED || state == PlayerState.PAUSED) {
            startPlayer()
        }
    }

    fun getTrack(): Track {
        val track = playerInteractor.getTrack()
        return track
    }

    override fun onCleared() {
        playerInteractor.release()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (player.isPlaying) {
                delay(300L)
                currentTime =
                    Utils.millisToSeconds(playerInteractor.getCurrentPosition().toLong())
                updateState()
            }
        }
    }

    fun addFavTrack(track: Track) {
        viewModelScope.launch {
            favTracksInteractor.insertFavTrack(DbConverter.map(track))
        }
    }

    fun deleteFavTrack(trackId: String?) {
        viewModelScope.launch {
            favTracksInteractor.deleteFavTrack(trackId)
        }
    }

    fun isFavorite(): Boolean = playerInteractor.isFavorite()

    fun syncFavorite() {
        currentIsFavorite = playerInteractor.isFavorite()
        updateState()
    }

    fun syncPlaylists() {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylists()
                .collect { playlists ->
                    currentPlaylists = playlists
                    updateState()
                }
        }

    }

    fun addToPlaylist(playlist: Playlist, track: Track) {

        viewModelScope.launch {
            playlistsInteractor.addToPlaylist(playlist, track)
            syncPlaylists()
        }
    }

}
