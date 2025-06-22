package com.example.playlistmaker.util

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.Year
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object Utils {

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun dpToPx(dp: Int, context: Context): Int {
        return dpToPx(dp.toFloat(), context)
    }


    fun millisToSeconds(milliseconds: Long?): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(milliseconds)

    fun millisToMinutes(milliseconds: Long?): String {
        if (milliseconds == null) return "0"
        val date = Date(milliseconds)
        val formatter = SimpleDateFormat("m", Locale.getDefault())
        return formatter.format(date)
    }

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


    inline fun <reified T> jsonFromList(list: List<T>): String {
        return Gson().toJson(list)
    }

    inline fun <reified T> listFromJson(json: String?): MutableList<T> {
        val type = object : com.google.gson.reflect.TypeToken<MutableList<T>>() {}.type
        return if (!json.isNullOrEmpty()) Gson().fromJson(json, type) else mutableListOf()
    }


    fun View.showStyledSnackbar(
        context: Context,
        message: String,
        fontRes: Int,
        backgroundColorRes: Int,
        textColorRes: Int,
        duration: Int = Snackbar.LENGTH_LONG,
    ) {
        val snackbar = Snackbar.make(this, message, duration)

        val snackbarView = snackbar.view
        snackbarView.background = ContextCompat.getDrawable(context, backgroundColorRes)

        val layoutParams = snackbarView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(dpToPx(8, context), 0, dpToPx(8, context), dpToPx(16, context))
        snackbarView.layoutParams = layoutParams

        val textView =
            snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val typeface = ResourcesCompat.getFont(context, fontRes)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        textView.typeface = typeface
        textView.setTextColor(ContextCompat.getColor(context, textColorRes))

        snackbar.show()
    }

    fun tracksCountEnding(count: Int): String {
        val lastTwoDigits = count % 100
        val lastDigit = count % 10
        if (lastTwoDigits in 11..14) {
            return "треков"
        }
        return when (lastDigit) {
            1 -> "трек"
            2, 3, 4 -> "трека"
            else -> "треков"
        }
    }

    fun minutesCountEnding(minutes: Int): String {
        val lastTwoDigits = minutes % 100
        val lastDigit = minutes % 10
        if (lastTwoDigits in 11..14) {
            return "минут"
        }
        return when (lastDigit) {
            1 -> "минута"
            2, 3, 4 -> "минуты"
            else -> "минут"
        }
    }

}


