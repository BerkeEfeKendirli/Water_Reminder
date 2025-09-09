package com.bek.waterreminder.util

import java.time.LocalDate
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
