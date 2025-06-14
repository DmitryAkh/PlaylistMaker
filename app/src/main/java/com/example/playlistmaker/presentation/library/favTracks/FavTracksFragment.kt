package com.example.playlistmaker.presentation.library.favTracks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavTracksFragment : Fragment() {

    private var _binding: FragmentFavoritesTracksBinding? = null
    private val binding get() = _binding!!
    private var adapter: FavTracksAdapter? = null
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private val viewModel by viewModel<FavTracksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesTracksBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadFavTracks()


        viewModel.observeFavTracks().observe(viewLifecycleOwner) { tracks ->

            if (tracks.isNotEmpty()) {
                binding.placeholderLibEmpty.isVisible = false
                adapter?.updateData(tracks)
                binding.rvFavTracks.isVisible = true
            } else {
                binding.placeholderLibEmpty.isVisible = true
                binding.rvFavTracks.isVisible = false
            }
        }


        onTrackClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
                viewModel.putTrackForPlayer(track)
                findNavController().navigate(R.id.action_libraryFragment_to_playerFragment3)
            }

        adapter = FavTracksAdapter(emptyList()) { track ->
            onTrackClickDebounce(track)
        }

        binding.rvFavTracks.adapter = adapter
        binding.rvFavTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavTracks()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}