package com.example.playlistmaker.data.dto


class TracksResponse(
    val results: List<TrackDto>,
    resultCode: Int,
) : Response(resultCode) {

}
