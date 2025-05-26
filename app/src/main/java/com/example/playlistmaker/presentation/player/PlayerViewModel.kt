package com.example.playlistmaker.presentation.player


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.domain.db.FavTracksInteractor
import com.example.playlistmaker.domain.interactors.PlayerInteractor
import com.example.playlistmaker.domain.entity.PlayerState
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favTracksInteractor: FavTracksInteractor,
) : ViewModel() {
    private val player = playerInteractor.getPlayer()
    private var timerJob: Job? = null
    private val stateLiveData = MutableLiveData(PlayerState.DEFAULT)
    private val timeLiveData: MutableLiveData<String> = MutableLiveData()
    private val isFavoriteLiveData: MutableLiveData<Boolean> = MutableLiveData()


    fun observeState(): LiveData<PlayerState> = stateLiveData
    fun observeTime(): LiveData<String> = timeLiveData
    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData

    fun preparePlayer() {
        playerInteractor.preparePlayer()
        playerInteractor.setOnCompletionListener {
            stateLiveData.postValue(PlayerState.PAUSED)
            timeLiveData.postValue("00:00")
            timerJob?.cancel()
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        stateLiveData.postValue(PlayerState.PLAYING)
        startTimer()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        stateLiveData.postValue(PlayerState.PAUSED)
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
//        isFavoriteLiveData.postValue(track.isFavorite)
        return track
    }
    override fun onCleared() {
        playerInteractor.release()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (player.isPlaying) {
                delay(300L)
                timeLiveData.postValue(
                    Utils.millisToSeconds(playerInteractor.getCurrentPosition().toLong())
                )
            }
        }
    }

    fun addFavTrack(track: Track) {
        viewModelScope.launch {
            favTracksInteractor.insertFavTrack(TrackDbConverter.map(track))
        }
    }

    fun deleteFavTrack(trackId: String?) {
        viewModelScope.launch {
            favTracksInteractor.deleteFavTrack(trackId)
        }
    }

    fun isFavorite(): Boolean = playerInteractor.isFavorite()

    fun syncFavorite() {
        isFavoriteLiveData.postValue(playerInteractor.isFavorite())
    }

}
