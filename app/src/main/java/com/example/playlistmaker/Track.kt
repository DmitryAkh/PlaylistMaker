package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val trackId: String,
) {

    companion object {
        fun formatTrackTime(milliseconds: Long): String {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(milliseconds)
        }

        fun convertTracksWithMillesToTracks(tracksWithMillesList: List<TrackWithMilles>): ArrayList<Track> {
            return ArrayList(tracksWithMillesList.map { tracksWithMilles ->
                Track(
                    trackName = tracksWithMilles.trackName,
                    artistName = tracksWithMilles.artistName,
                    trackTime = formatTrackTime(tracksWithMilles.trackTimeMillis),
                    artworkUrl100 = tracksWithMilles.artworkUrl100,
                    trackId = tracksWithMilles.trackId
                )
            })
        }
    }


}

data class TrackWithMilles(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: String,
)