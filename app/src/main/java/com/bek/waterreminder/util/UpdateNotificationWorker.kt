package com.bek.waterreminder.util

import WaterReminderWorker
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun updateNotificationWorker(context: Context, intervalMinutes: Long) {
  val safeInterval = maxOf(intervalMinutes, 15)
  val workRequest =
      PeriodicWorkRequestBuilder<WaterReminderWorker>(safeInterval, TimeUnit.MINUTES).build()
  WorkManager.getInstance(context)
      .enqueueUniquePeriodicWork(
          "water_reminder_work",
          ExistingPeriodicWorkPolicy.UPDATE,
          workRequest,
      )
}
