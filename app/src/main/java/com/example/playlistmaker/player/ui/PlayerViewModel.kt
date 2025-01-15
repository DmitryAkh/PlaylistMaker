package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Utils

class PlayerViewModel() : ViewModel() {

    private val stateLiveData = MutableLiveData<PlayerState>()
    private val timeLiveData = MutableLiveData<String>()
    private val handler = Handler(Looper.getMainLooper())
    private val interactor = Creator.providePlayerInteractor()

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            timeLiveData.postValue(
                Utils.formatTrackTime(interactor.getCurrentPosition().toLong())
            )
            handler.postDelayed(this, 100)
        }
    }


    fun observeState(): LiveData<PlayerState> = stateLiveData
    fun observeTime(): LiveData<String> = timeLiveData

    fun preparePlayer(track: Track?) {
        handler.removeCallbacks(updateTimerRunnable)
        interactor.preparePlayer(track)
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

    override fun onCleared() {
        handler.removeCallbacks(updateTimerRunnable)
        interactor.release()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    PlayerViewModel()
                }
            }
    }
}

