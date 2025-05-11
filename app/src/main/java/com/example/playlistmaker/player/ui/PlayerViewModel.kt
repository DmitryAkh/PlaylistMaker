package com.example.playlistmaker.player.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.util.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(
    private val interactor: PlayerInteractor,
) : ViewModel() {
    private val player = interactor.getPlayer()
    private var timerJob: Job? = null
    private val stateLiveData = MutableLiveData(PlayerState.DEFAULT)
    private val timeLiveData: MutableLiveData<String> = MutableLiveData()


    fun observeState(): LiveData<PlayerState> = stateLiveData
    fun observeTime(): LiveData<String> = timeLiveData

    fun preparePlayer() {
        interactor.preparePlayer()
        interactor.setOnCompletionListener {
            stateLiveData.postValue(PlayerState.PAUSED)
            timeLiveData.postValue("00:00")
            timerJob?.cancel()
        }
    }

    private fun startPlayer() {
        interactor.startPlayer()
        stateLiveData.postValue(PlayerState.PLAYING)
        startTimer()
    }

    fun pausePlayer() {
        interactor.pausePlayer()
        stateLiveData.postValue(PlayerState.PAUSED)
    }

    fun togglePlayer() {
        val state = interactor.getState()

        if (state == PlayerState.PLAYING) {
            pausePlayer()
        } else if (state == PlayerState.PREPARED || state == PlayerState.PAUSED) {
            startPlayer()
        }
    }

    fun getTrack(): Track = interactor.getTrack()

    override fun onCleared() {
        interactor.release()
    }


    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (player.isPlaying) {
                delay(300L)
                timeLiveData.postValue(
                    Utils.millisToSeconds(interactor.getCurrentPosition().toLong())
                )
            }
        }
    }

}
