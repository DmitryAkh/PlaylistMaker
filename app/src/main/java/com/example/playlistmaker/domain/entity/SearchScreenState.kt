package com.example.playlistmaker.domain.entity

data class SearchScreenState(
    val searchState: SearchState,
    val tracks: List<Track>,
    val history: List<Track>,
)