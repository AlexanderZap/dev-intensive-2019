package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd:MM:yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.shortFormat(): String {
    val pattern = if(this.isSameFormat(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))

    return dateFormat.format(this)
}

private fun Date.isSameFormat(date: Date): Boolean {
    val day1 = this.time / DAY
    val day2 = date.time / DAY

    return day1 == day2
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time

    return this
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(number: Int): String {

        val minutes = number / (60 * 1000)
        val hours = number / (3600 * 1000)
        val days = number / (24 * 3600 * 1000)

        when (this) {

            MINUTE -> {
                return if (minutes.toString() == "1") "$minutes минуту"
                else if (minutes.toString() == "2" || minutes.toString() == "3" || minutes.toString() == "4") "$minutes минуты"
                else "$minutes минут"

            }
            HOUR -> {
                return if (hours.toString() == "1") "$hours час"
                else if (hours.toString() == "2" || hours.toString() == "3" || hours.toString() == "4") "$hours часа"
                else "$hours часов"
            }
            DAY -> {
                return if (days.toString() == "1") "$days день"
                else if (days.toString() == "2" || days.toString() == "3" || days.toString() == "4") "$days дня"
                else "$days дней"
            }
            else -> return ""
        }
    }
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val dif = kotlin.math.abs(this.time - date.time)

    return when {
        dif <= SECOND -> "только что"
        dif <= SECOND * 55 -> "несколько секунд назад"
        dif <= MINUTE -> "минуту назад"
        dif <= MINUTE * 55 -> "${TimeUnits.MINUTE.plural(dif.toInt())} назад"
        dif <= HOUR -> "час назад"
        dif <= HOUR * 23 -> "${TimeUnits.HOUR.plural(dif.toInt())} назад"
        dif <= DAY -> "день назад"
        dif <= DAY * 364 -> "${TimeUnits.DAY.plural(dif.toInt())} назад"
        else -> "более года назад"
    }
}