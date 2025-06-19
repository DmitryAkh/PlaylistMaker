package com.example.playlistmaker.presentation.library.playlists

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R

class PlaylistInternals : Fragment() {

    companion object {
        fun newInstance() = PlaylistInternals()
    }

    private val viewModel: PlaylistInternalsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_playlist_internals, container, false)
    }
}