package com.bek.waterreminder.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {

  private val _sleepStartHourKey = intPreferencesKey("sleep_start_hour")
  private val _sleepStartMinuteKey = intPreferencesKey("sleep_start_minute")
  private val _sleepEndHourKey = intPreferencesKey("sleep_end_hour")
  private val _sleepEndMinuteKey = intPreferencesKey("sleep_end_minute")
  private val _notificationIntervalKey = longPreferencesKey("notification_interval_minutes")

  // Flows
  val sleepStartHour: Flow<Int> = dataStore.data.map { it[_sleepStartHourKey] ?: 23 }

  val sleepStartMinute: Flow<Int> = dataStore.data.map { it[_sleepStartMinuteKey] ?: 0 }

  val sleepEndHour: Flow<Int> = dataStore.data.map { it[_sleepEndHourKey] ?: 7 }

  val sleepEndMinute: Flow<Int> = dataStore.data.map { it[_sleepEndMinuteKey] ?: 0 }

  val notificationIntervalMinutes: Flow<Long> =
      dataStore.data.map { it[_notificationIntervalKey] ?: 60L }

  // Setters
  suspend fun setSleepStart(hour: Int, minute: Int) {
    dataStore.edit {
      it[_sleepStartHourKey] = hour
      it[_sleepStartMinuteKey] = minute
    }
  }

  suspend fun setSleepEnd(hour: Int, minute: Int) {
    dataStore.edit {
      it[_sleepEndHourKey] = hour
      it[_sleepEndMinuteKey] = minute
    }
  }

  suspend fun setNotificationInterval(hour: Int, minute: Int) {
    val value = hour * 60L + minute
    dataStore.edit { it[_notificationIntervalKey] = value }
  }
}
