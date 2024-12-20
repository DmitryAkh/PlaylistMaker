package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.domain.models.ResponseState

interface NetworkClient {
    fun doRequest(dto: Any): Response
    fun getResponseState(): ResponseState
}