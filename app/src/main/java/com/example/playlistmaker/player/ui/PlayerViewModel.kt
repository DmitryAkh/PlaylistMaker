package com.example.playlistmaker.player.ui

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.util.Utils

class PlayerViewModel(
    private val stateLiveData: MutableLiveData<PlayerState>,
    private val timeLiveData: MutableLiveData<String>,
    private val handler: Handler,
    private val interactor: PlayerInteractor,
) : ViewModel() {


    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            timeLiveData.postValue(
                Utils.millisToSeconds(interactor.getCurrentPosition().toLong())
            )
            handler.postDelayed(this, 100)
        }
    }


    fun observeState(): LiveData<PlayerState> = stateLiveData
    fun observeTime(): LiveData<String> = timeLiveData

    fun preparePlayer() {
        handler.removeCallbacks(updateTimerRunnable)
        interactor.preparePlayer()
        interactor.setOnCompletionListener {
            stateLiveData.postValue(PlayerState.PAUSED)
        }
        timeLiveData.postValue("00:00")
    }

    fun startPlayer() {
        interactor.startPlayer()
        handler.post(updateTimerRunnable)
        stateLiveData.postValue(PlayerState.PLAYING)
    }

    fun pausePlayer() {
        interactor.pausePlayer()
        stateLiveData.postValue(PlayerState.PAUSED)
    }

    fun togglePlayer() {
        if (interactor.getState() == PlayerState.PLAYING) {

            pausePlayer()
        } else if (interactor.getState() == PlayerState.PREPARED || interactor.getState() == PlayerState.PAUSED)

            startPlayer()
    }

    fun getTrack(): Track = interactor.getTrack()

    override fun onCleared() {
        handler.removeCallbacks(updateTimerRunnable)
        interactor.release()
    }

    companion object {
        fun getViewModelFactory(
            stateLiveData: MutableLiveData<PlayerState>,
            timeLiveData: MutableLiveData<String>,
            handler: Handler,
            interactor: PlayerInteractor,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    PlayerViewModel(stateLiveData, timeLiveData, handler, interactor)
                }
            }
    }
}

