package br.com.mdr.base.extension

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val BR_DATE_FORMAT = "dd/MM/yyyy"

object BRDateFormatter : SimpleDateFormat(BR_DATE_FORMAT, Locale.getDefault())
fun String.asBrazilianDate(): Date? {
    return try {
        BRDateFormatter.isLenient = false
        BRDateFormatter.parse(this)
    } catch (error: ParseException) {
        Timber.tag("DateExtensions").e(error)
        null
    }
}

fun Date.yearFromDate(): Int {
    val cal = Calendar.getInstance(Locale.getDefault())
    cal.time = this
    return cal[Calendar.YEAR]
}

fun Date.monthFromDate(): Int {
    val cal = Calendar.getInstance(Locale.getDefault())
    cal.time = this
    return cal[Calendar.MONTH]
}