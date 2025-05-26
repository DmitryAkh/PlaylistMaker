package com.example.playlistmaker.util

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.time.Year
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Utils {
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()


    }

    fun millisToSeconds(milliseconds: Long?): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(milliseconds)

    fun secondsToMillis(time: String): Long {
        val parts = time.split(":")
        val minutes = parts.getOrNull(0)?.toLongOrNull() ?: 0L
        val seconds = parts.getOrNull(1)?.toLongOrNull() ?: 0L
        return (minutes * 60 + seconds) * 1000
    }


    fun formatDate(date: String?): String {
        return if (!date.isNullOrBlank()) {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy")
                when {
                    date.length == 4 -> {
                        Year.parse(date).format(formatter)
                    }

                    else -> {
                        ZonedDateTime.parse(date).format(formatter)
                    }
                }
            } catch (e: Exception) {
                ""
            }
        } else {
            ""
        }
    }

    fun getCoverArtwork(url: String?) = url?.replaceAfterLast('/', "512x512bb.jpg")


}


