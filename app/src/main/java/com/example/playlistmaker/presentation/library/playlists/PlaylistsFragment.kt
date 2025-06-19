package com.example.playlistmaker.presentation.library.playlists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private var adapter: PlaylistsAdapter? = null
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit
    private val viewModel by viewModel<PlaylistsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        onPlaylistClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { playlist ->
// TODO:
            }

        adapter = PlaylistsAdapter(emptyList()) { playlist ->
            onPlaylistClickDebounce(playlist)
        }

        val layoutManager = GridLayoutManager(context, 2)
        binding.rvPlaylists.layoutManager = layoutManager

        binding.rvPlaylists.adapter = adapter

        binding.placeholderButtonNew.setOnClickListener {

            findNavController().navigate(R.id.action_libraryFragment_to_newPlayListFragment)
        }


        viewModel.getPlaylists()

        viewModel.observePlaylists().observe(viewLifecycleOwner) { playlists ->
            if (playlists.isNotEmpty()) {
                binding.placeholderLibEmpty.isVisible = false
                adapter?.updateData(playlists)
                binding.rvPlaylists.isVisible = true
            } else {
                binding.placeholderLibEmpty.isVisible = true
                binding.rvPlaylists.isVisible = false
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}