package com.example.playlistmaker.util

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
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

    fun formatDate(date: String?): String {
        return if (date != null) {
            val zonedDateTime = ZonedDateTime.parse(date)
            val formatter = DateTimeFormatter.ofPattern("yyyy")
            zonedDateTime.format(formatter)
        } else {
            ""
        }
    }

    fun getCoverArtwork(url: String?) = url?.replaceAfterLast('/', "512x512bb.jpg")


}


