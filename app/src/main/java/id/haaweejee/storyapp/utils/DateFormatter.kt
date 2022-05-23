package id.haaweejee.storyapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object DateFormatter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateAndTimeO(currentDateString: String, targetTimeZone: String) : String{
        val instant = Instant.parse(currentDateString)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
            .withZone(ZoneId.of(targetTimeZone))
        return formatter.format(instant)
    }

    fun formatDateAndTime(time: String): String {
        val locale = Locale("in", "ID")
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)

        return try {
            timestamp.format(time)
        } catch (ex: Exception) {
            "-"
        }
    }
}