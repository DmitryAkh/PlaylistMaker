package com.example.playlistmaker.presentation.library.playlists

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.library.playlists.internals.PlaylistInternalsFragment.Companion.ARGUMENTS
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : NewPlayListFragment() {


    private val viewModel by viewModel<EditPlaylistViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistId = arguments?.getInt(ARGUMENTS)


        viewModel.getPlaylist(playlistId)
        viewModel.observeScreenState().observe(viewLifecycleOwner) { state ->
            binding.cover.setImageURI(state.playlist.coverPath.toUri())
            binding.editName.setText(state.playlist.playlistName)
            binding.editDescription.setText(state.playlist.playlistDescription)
        }

        binding.headerText.text = context?.getString(R.string.edit) ?: ""
        binding.createButton.text = context?.getString(R.string.save)
        binding.cover.isVisible = true

        binding.createButton.setOnClickListener {
            updatePlaylistData(playlistId!!, name, description, path)
            findNavController().navigateUp()
        }

    }

    private fun updatePlaylistData(
        playlistId: Int,
        name: String,
        description: String,
        coverPath: String,
    ) {
        viewModel.updatePlaylistData(playlistId, name, description, coverPath)
    }


}