package com.example.playlistmaker.search.domain


class SearchInteractorImpl(private val repository: SearchRepository) :
    SearchInteractor {


    override fun addTrackToHistory(track: Track) = repository.addTrackToHistory(track)

    override fun clearHistoryList() = repository.clearHistoryList()

    override fun loadHistoryList(): List<Track> = repository.loadHistoryList()

    override fun historyListFromJson(json: String?): MutableList<Track> =
        repository.historyListFromJson(json)

    override fun jsonFromHistoryList(historyList: MutableList<Track>): String =
        repository.jsonFromHistoryList(historyList)

    override fun doSearch(expression: String, consumer: SearchInteractor.TracksConsumer) {
        when (val resource = repository.doSearch(expression)) {
            is Resource.Success -> consumer.consume(resource.data, null)
            is Resource.Error -> consumer.consume(null, resource.message)
        }

    }

    override fun getResponseState(): ResponseState = repository.getResponseState()

    override fun putTrackForPlayer(track: Track) = repository.putTrackForPlayer(track)


}