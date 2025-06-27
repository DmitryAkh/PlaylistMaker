package com.example.playlistmaker.presentation.library.playlists.internals

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistInternalsBinding
import com.example.playlistmaker.domain.entity.PlaylistInternalsState
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.Utils
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistInternalsFragment : Fragment() {

    private var _binding: FragmentPlaylistInternalsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistInternalsViewModel>()
    private var adapter: PlaylistInternalsAdapter? = null
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetBehaviorOptions: BottomSheetBehavior<ConstraintLayout>
    private lateinit var recyclerView: RecyclerView
    private var playlistId: Int? = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistInternalsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistId = arguments?.getInt(ARGUMENTS)
        setupObserver()
        prepareUI()
        onTrackClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
                viewModel.putTrackForPlayer(track)
                findNavController().navigate(R.id.action_playlistInternals_to_playerFragment3)
            }

        bottomSheetBehaviorOptions.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.overlay.isVisible = true
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.isVisible = false
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        binding.overlay.isVisible = true
                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        binding.overlay.isVisible = true
                    }

                    BottomSheetBehavior.STATE_SETTLING -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

    }


    private fun prepareUI() {
        recyclerView = binding.rvBottomSheet.rvTracks

        adapter = PlaylistInternalsAdapter(
            onTrackClicked = { track ->
                onTrackClickDebounce(track)
            },
            onTrackHold = { track ->
                confirmDelete(track)
            }

        )

        binding.rvBottomSheet.rvTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBottomSheet.rvTracks.adapter = adapter
        bottomSheetBehavior = BottomSheetBehavior.from(binding.rvBottomSheet.root).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehaviorOptions = BottomSheetBehavior.from(binding.insideOptions.root).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }


        binding.backButton.setOnClickListener {
            findNavController().navigateUp()

        }

    }

    private fun showContent() {
        binding.rvBottomSheet.placeholder.isVisible = false
        binding.rvBottomSheet.rvTracks.isVisible = true
    }

    private fun showPlaceholder() {
        binding.rvBottomSheet.placeholder.isVisible = true
        binding.rvBottomSheet.rvTracks.isVisible = false
    }

    private fun render(state: PlaylistInternalsState) {
        when (state) {
            PlaylistInternalsState.CONTENT -> showContent()
            PlaylistInternalsState.EMPTY -> showPlaceholder()
        }

    }

    private fun setupObserver() {
        viewModel.getPlaylist(playlistId)
        viewModel.observeScreenState().observe(viewLifecycleOwner) { state ->

            val playlist = state.playlist
            val tracks = state.playlist.tracks
            val countOfTracks = getString(
                R.string.track_count,
                playlist.tracksCount,
                Utils.tracksCountEnding(playlist.tracksCount)
            )
            val (totalMinutes, totalMillis) = viewModel.calculateMinutes(tracks)



            adapter?.submitList(state.tracklist)
            render(state.playlistState)



            Glide.with(this)
                .load(playlist.coverPath)
                .centerCrop()
                .placeholder(R.drawable.placeholder312)
                .into(binding.cover)

            binding.name.text = playlist.playlistName
            binding.count.text = countOfTracks
            binding.description.text = playlist.playlistDescription
            binding.duration.text = getString(
                R.string.track_duration,
                totalMinutes,
                Utils.minutesCountEnding(totalMillis)
            )
            binding.share.setOnClickListener {
                sharePlaylist(tracks, playlist.playlistName, playlist.playlistDescription)
            }

            Glide.with(this)
                .load(playlist.coverPath)
                .centerCrop()
                .placeholder(R.drawable.placeholder312)
                .into(binding.insideOptions.cover)

            binding.insideOptions.name.text = playlist.playlistName
            binding.insideOptions.count.text = countOfTracks

            binding.options.setOnClickListener {
                bottomSheetBehaviorOptions.state = BottomSheetBehavior.STATE_EXPANDED
            }

            binding.overlay.setOnClickListener {
                bottomSheetBehaviorOptions.state = BottomSheetBehavior.STATE_HIDDEN
            }

            binding.insideOptions.share.setOnClickListener {
                sharePlaylist(tracks, playlist.playlistName, playlist.playlistDescription)

            }

            binding.insideOptions.edit.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("PLAYLIST_ID", playlist.playlistId)
                }
                findNavController().navigate(
                    R.id.action_playlistInternals_to_editPlaylistFragment,
                    bundle
                )
            }

            binding.insideOptions.delete.setOnClickListener {
                deletePlaylist(playlist.tracks, playlistId!!)
            }

        }
    }

    private fun confirmDelete(track: Track) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_the_track)
            .setMessage(R.string.you_want_to_delete_the_track)
            .setNeutralButton(R.string.yes) { dialog, witch ->
                viewModel.deleteTrackFromPlaylist(track.trackId)
            }.setNegativeButton(R.string.no) { dialog, witch ->
            }.show()
    }

    private fun showNoTracksMessage() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.no_tracks_for_sharing)
            .setNeutralButton(R.string.ok) { dialog, witch ->
            }.show()
    }


    private fun sharePlaylist(
        trackList: List<Track>,
        playlistName: String,
        playlistDescription: String,
    ) {

        if (trackList.isNotEmpty()) {
            val message =
                buildString {
                    appendLine(playlistName)
                    appendLine(playlistDescription)
                    appendLine("${trackList.size} ${Utils.tracksCountEnding(trackList.size)}")
                    appendLine()

                    trackList.forEachIndexed { index, track ->
                        val duration = track.trackTime ?: "0:00"
                        appendLine("${index + 1}. ${track.artistName} - ${track.trackName} ($duration)")
                    }
                }

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }

            startActivity(Intent.createChooser(shareIntent, "Поделиться плейлистом"))
        } else {
            showNoTracksMessage()
        }


    }

    private fun deletePlaylist(tracksIds: List<Track>, playlistId: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_playlist)
            .setMessage(R.string.you_want_to_delete_the_playlist)
            .setNeutralButton(R.string.yes) { dialog, witch ->
                findNavController().navigateUp()
                viewModel.deletePlaylist(tracksIds, playlistId)
            }.setNegativeButton(R.string.no) { dialog, witch ->
            }.show()

    }

    companion object {
        const val ARGUMENTS = "PLAYLIST_ID"
        private const val CLICK_DEBOUNCE_DELAY = 500L

    }

}