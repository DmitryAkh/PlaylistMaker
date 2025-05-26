package com.example.playlistmaker.presentation.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.presentation.library.favTracks.FavTracksFragment
import com.example.playlistmaker.presentation.library.playlists.PlaylistsFragment

class LibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavTracksFragment()
            else -> PlaylistsFragment()
        }
    }

    companion object {
        private const val ITEM_COUNT = 2
    }

}