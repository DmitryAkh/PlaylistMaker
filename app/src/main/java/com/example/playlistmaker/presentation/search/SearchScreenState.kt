package com.example.playlistmaker.presentation.search

sealed interface SearchScreenState {
    data object Loading : SearchScreenState
    data object Content : SearchScreenState
    data object History : SearchScreenState
    data object NoInternet : SearchScreenState
    data object NotFound : SearchScreenState
}