package com.bek.waterreminder.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun getLast7DaysLabels(): List<String> {
  return (6 downTo 0).map { offset ->
    LocalDate.now()
        .minusDays(offset.toLong())
        .dayOfWeek
        .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
  }
}

fun formatDayLabel(date: LocalDate): String {
  val today = LocalDate.now()
  val yesterday = today.minusDays(1)
  return when (date) {
    today -> "Today"
    yesterday -> "Yesterday"
    else -> date.format(DateTimeFormatter.ofPattern("MMM dd, yy"))
  }
}
