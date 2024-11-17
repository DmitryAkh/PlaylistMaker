package com.example.playlistmaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class Track(
    val trackName: String?,
    val artistName: String?,
    val trackTime: String?,
    val artworkUrl100: String?,
    val trackId: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
) : Parcelable {

    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")


    companion object {

        fun formatDate(date: String?): String {
            return if (date != null) {
                val zonedDateTime = ZonedDateTime.parse(date)
                val formatter = DateTimeFormatter.ofPattern("yyyy")
                zonedDateTime.format(formatter)
            } else {
                ""
            }
        }

        fun convertTracksWithMillesToTracks(tracksWithMillesList: List<TrackWithMilles>): ArrayList<Track> {
            return ArrayList(tracksWithMillesList.map { tracksWithMilles ->
                Track(
                    trackName = tracksWithMilles.trackName,
                    artistName = tracksWithMilles.artistName,
                    trackTime = Utils.formatTrackTime(tracksWithMilles.trackTimeMillis),
                    artworkUrl100 = tracksWithMilles.artworkUrl100,
                    trackId = tracksWithMilles.trackId,
                    collectionName = tracksWithMilles.collectionName,
                    releaseDate = formatDate(tracksWithMilles.releaseDate),
                    primaryGenreName = tracksWithMilles.primaryGenreName,
                    country = tracksWithMilles.country,
                    previewUrl = tracksWithMilles.previewUrl
                )
            })
        }


    }


}

data class TrackWithMilles(
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,
    val trackId: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
)