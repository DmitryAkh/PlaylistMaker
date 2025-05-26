package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.util.Utils

class PlayerViewModel(
    private val interactor: PlayerInteractor,
) : ViewModel() {
    private val timeLiveData: MutableLiveData<String> = MutableLiveData()
    private val stateLiveData: MutableLiveData<PlayerState> = MutableLiveData()
    private val handler: Handler = Handler(Looper.getMainLooper())


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
            handler.removeCallbacks(updateTimerRunnable)
            timeLiveData.postValue("00:00")
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

}

