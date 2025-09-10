package com.bek.waterreminder.util

import WaterReminderWorker
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun updateNotificationWorker(context: Context, intervalMinutes: Long) {
  val safeInterval = maxOf(intervalMinutes, 15L)
  val workRequest =
      PeriodicWorkRequestBuilder<WaterReminderWorker>(
              repeatInterval = safeInterval,
              repeatIntervalTimeUnit = TimeUnit.MINUTES,
          )
          .build()

  WorkManager.getInstance(context)
      .enqueueUniquePeriodicWork(
          "water_reminder_work",
          ExistingPeriodicWorkPolicy.UPDATE,
          workRequest,
      )
}
