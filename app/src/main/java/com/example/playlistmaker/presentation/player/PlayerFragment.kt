package com.example.playlistmaker.presentation.player

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.entity.PlayerState
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.Utils
import com.example.playlistmaker.util.Utils.showStyledSnackbar
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var track: Track
    private lateinit var overlay: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetAdapter: BottomSheetAdapter
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.syncPlaylists()
        setupObservers()
        track = viewModel.getTrack()
        viewModel.syncFavorite()
        prepareUI()



        onPlaylistClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { playlist ->
                if (playlist.tracksIds.contains(track)) {
                    binding.root.showStyledSnackbar(
                        requireContext(),
                        getString(R.string.track_has_already_been_added, playlist.playlistName),
                        R.font.ys_display_regular,
                        R.color.main,
                        R.color.main_inverse
                    )
                } else {
                    viewModel.addToPlaylist(playlist, track)
                    binding.root.showStyledSnackbar(
                        requireContext(),
                        getString(R.string.added_to_playlist_successfully, playlist.playlistName),
                        R.font.ys_display_regular,
                        R.color.main,
                        R.color.main_inverse
                    )
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    binding.overlay.isVisible = false
                }
            }


        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun prepareUI() {

        recyclerView = binding.bottomSheet.rvBottomSheet
        overlay = binding.overlay

        bottomSheetAdapter = BottomSheetAdapter(emptyList()) { playlist ->
            onPlaylistClickDebounce(playlist)
        }

        binding.bottomSheet.rvBottomSheet.layoutManager = LinearLayoutManager(requireContext())
        binding.bottomSheet.rvBottomSheet.adapter = bottomSheetAdapter
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.preparePlayer()

        Glide.with(this)
            .load(Utils.getCoverArtwork(track.artworkUrl100))
            .centerCrop()
            .transform(RoundedCorners(Utils.dpToPx(8f, requireContext())))
            .placeholder(R.drawable.placeholder312)
            .into(binding.cover)

        binding.artistName.text = track.artistName
        binding.trackName.text = track.trackName
        binding.trackTimeData.text = track.trackTime
        binding.trackAlbumData.text = track.collectionName
        binding.trackYearData.text = track.releaseDate
        binding.trackGenreData.text = track.primaryGenreName
        binding.trackCountryData.text = track.country
        binding.playButton.setOnClickListener {
            viewModel.togglePlayer()
        }

        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            overlay.visibility = View.VISIBLE
        }

        binding.likeButton.setOnClickListener {
            if (viewModel.isFavorite()) {
                track.isFavorite = false
                viewModel.deleteFavTrack(track.trackId)
            } else {
                track.isFavorite = true
                viewModel.addFavTrack(track)
            }
            viewModel.syncFavorite()
        }

        overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.overlay.isVisible = false
        }

        binding.bottomSheet.buttonNew.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment3_to_newPlayListFragment)
        }
    }

    private fun setupObservers() {


        viewModel.observeScreenState().observe(viewLifecycleOwner) { state ->

            binding.playerTime.text = state.time


            val playRes = if (state.playerState == PlayerState.PLAYING) {
                R.drawable.pause_button
            } else {
                R.drawable.play_button
            }

            binding.playButton.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), playRes)
            )

            val likeRes = if (state.isFavorite) {
                R.drawable.like_button_active
            } else {
                R.drawable.like_button_inactive
            }

            binding.likeButton.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), likeRes)
            )



            bottomSheetAdapter.updateData(state.playlists)


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
