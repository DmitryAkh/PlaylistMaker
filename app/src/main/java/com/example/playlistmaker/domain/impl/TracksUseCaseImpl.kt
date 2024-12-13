package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.api.TracksUseCase

class TracksUseCaseImpl(private val repository: TracksRepository) : TracksUseCase {

    override fun doSearch(expression: String, consumer: TracksUseCase.TracksConsumer) {
        val t = Thread {
            consumer.consume(repository.doSearch(expression))

        }
        t.start()

    }

    override fun getResultCode(): Int {
        return repository.getResultCode()
    }

}