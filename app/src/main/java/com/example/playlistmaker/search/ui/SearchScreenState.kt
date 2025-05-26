package com.example.playlistmaker.search.ui

sealed interface SearchScreenState {
    data object Loading : SearchScreenState
    data object Content : SearchScreenState
    data object History : SearchScreenState
    data object NoInternet : SearchScreenState
    data object NotFound : SearchScreenState
}