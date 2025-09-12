package com.bek.waterreminder.util

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.time.LocalTime

val Context.dataStore by preferencesDataStore(name = "settings")

class WaterReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
  override fun doWork(): Result {
    val prefs = applicationContext.dataStore

    val sleepStartHour = runBlocking {
      prefs.data.map { it[intPreferencesKey("sleep_start_hour")] ?: 23 }.first()
    }
    val sleepStartMinute = runBlocking {
      prefs.data.map { it[intPreferencesKey("sleep_start_minute")] ?: 0 }.first()
    }
    val sleepEndHour = runBlocking {
      prefs.data.map { it[intPreferencesKey("sleep_end_hour")] ?: 7 }.first()
    }
    val sleepEndMinute = runBlocking {
      prefs.data.map { it[intPreferencesKey("sleep_end_minute")] ?: 0 }.first()
    }

    val now = LocalTime.now()
    val sleepStart = LocalTime.of(sleepStartHour, sleepStartMinute)
    val sleepEnd = LocalTime.of(sleepEndHour, sleepEndMinute)

    val isSleepTime =
        when {
          sleepStart.isBefore(sleepEnd) -> {
            now.isAfter(sleepStart) && now.isBefore(sleepEnd)
          }
          sleepStart.isAfter(sleepEnd) -> {
            now.isAfter(sleepStart) || now.isBefore(sleepEnd)
          }
          else -> false
        }

    if (isSleepTime) {
      return Result.success()
    }

    NotificationHelper.showNotification(
        context = applicationContext,
        title = "Time to drink water!",
        message = "Stay hydrated 💧",
    )
    return Result.success()
  }
}
