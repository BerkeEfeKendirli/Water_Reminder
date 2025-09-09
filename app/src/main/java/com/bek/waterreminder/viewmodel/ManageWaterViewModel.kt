package com.bek.waterreminder.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import com.bek.waterreminder.data.model.managewater.WaterEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ManageWaterViewModel(private val _dataStore: DataStore<Preferences>) : ViewModel() {
  private val _dailyGoalKey = intPreferencesKey("daily_goal")
  private val _streakKey = intPreferencesKey("streak_count")
  private val _lastCompletedDateKey = intPreferencesKey("last_completed_date")
  private val _weightKey = floatPreferencesKey("user_weight")
  private val _selectedCupKey = intPreferencesKey("selected_cup")

  val selectedCupFlow: Flow<Int> =
      _dataStore.data.map { preferences -> preferences[_selectedCupKey] ?: 200 }

  val dailyWaterFlow: Flow<Int> =
      _dataStore.data.map { preferences ->
        val todayKey = getWaterKeyForToday()
        preferences[todayKey] ?: 0
      }

  val weightFlow: Flow<Float> = _dataStore.data.map { preferences -> preferences[_weightKey] ?: 0f }

  val dailyGoalFlow: Flow<Int> =
      _dataStore.data.map { preferences -> preferences[_dailyGoalKey] ?: 2000 }

  val streakFlow: Flow<Int> = _dataStore.data.map { preferences -> preferences[_streakKey] ?: 0 }

  val todayWaterEntriesFlow: Flow<List<WaterEntry>> =
      _dataStore.data.map { preferences ->
        val key = getWaterEntriesKeyForToday()
        val json = preferences[key] ?: "[]"
        Json.decodeFromString<List<WaterEntry>>(json)
      }

  val weeklyWaterFlow: Flow<List<Int>> =
      _dataStore.data.map { preferences ->
        val days =
            (0..6).map { offset ->
              val date = LocalDate.now().minusDays((6 - offset).toLong())
              val key =
                  intPreferencesKey(
                      "water_${date.year * 10000 + date.monthValue*100+ date.dayOfMonth}"
                  )
              preferences[key] ?: 0
            }
        days
      }

  suspend fun addWaterEntryToday(amount: Int) {
    val key = getWaterEntriesKeyForToday()
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("hh:mma")
    val formattedTime = currentTime.format(formatter)
    val entry = WaterEntry(amount, formattedTime)

    val currentJson = _dataStore.data.first()[key] ?: "[]"
    val currentList = Json.decodeFromString<List<WaterEntry>>(currentJson)
    val newList = currentList + entry
    val newJson = Json.encodeToString(newList)
    _dataStore.edit { it[key] = newJson }
    drinkWater(amount)
  }

  suspend fun removeWaterEntryToday(index: Int) {
    val key = getWaterEntriesKeyForToday()
    val currentJson = _dataStore.data.first()[key] ?: "[]"
    val currentList = Json.decodeFromString<List<WaterEntry>>(currentJson)
    if (index in currentList.indices) {
      val removedEntry = currentList[index]
      val newList = currentList.toMutableList().apply { removeAt(index) }
      val newJson = Json.encodeToString(newList)
      _dataStore.edit { it[key] = newJson }
      decreaseWater(removedEntry.amount)
    }
  }

  suspend fun checkAndUpdateStreak() {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val yesterdayInt = yesterday.year * 10000 + yesterday.monthValue * 100 + yesterday.dayOfMonth
    val dailyGoal = _dataStore.data.first()[_dailyGoalKey] ?: 2000
    val yesterdayWaterKey = intPreferencesKey("water_$yesterdayInt")
    val yesterdayWater = _dataStore.data.first()[yesterdayWaterKey] ?: 0

    if (yesterdayWater < dailyGoal) {
      updateStreak(0)
    }
  }

  suspend fun updateDailyGoal(newGoal: Int) {
    _dataStore.edit { preferences -> preferences[_dailyGoalKey] = newGoal }
  }

  suspend fun updateStreak(newStreak: Int) {
    _dataStore.edit { preferences -> preferences[_streakKey] = newStreak }
  }

  suspend fun updateSelectedCup(newCup: Int) {
    _dataStore.edit { preferences -> preferences[_selectedCupKey] = newCup }
  }

  suspend fun updateLastCompletedDate(newDate: Int) {
    _dataStore.edit { preferences -> preferences[_lastCompletedDateKey] = newDate }
  }

  fun getWaterKeyForToday(): Preferences.Key<Int> {
    val today = getTodayAsInt()
    return intPreferencesKey("water_$today")
  }

  fun getWaterEntriesKeyForToday(): Preferences.Key<String> {
    val today = getTodayAsInt()
    return stringPreferencesKey("water_entries_$today")
  }

  suspend fun drinkWater(amount: Int) {
    val waterKey = getWaterKeyForToday()
    val currentWater = _dataStore.data.first()[waterKey] ?: 0
    val dailyGoal = _dataStore.data.first()[_dailyGoalKey] ?: 2000
    val newWater = currentWater + amount
    _dataStore.edit { preferences -> preferences[waterKey] = newWater }

    val todayDateInt = getTodayAsInt()
    val lastDate = _dataStore.data.first()[_lastCompletedDateKey] ?: 0
    val streak = _dataStore.data.first()[_streakKey] ?: 0

    if (newWater >= dailyGoal && lastDate != todayDateInt) {
      updateStreak(streak + 1)
      updateLastCompletedDate(todayDateInt)
    }
  }

  suspend fun decreaseWater(amount: Int) {
    val waterKey = getWaterKeyForToday()
    val currentWater = _dataStore.data.first()[waterKey] ?: 0
    val dailyGoal = _dataStore.data.first()[_dailyGoalKey] ?: 2000
    val newWater = (currentWater - amount).coerceAtLeast(0)
    _dataStore.edit { preferences -> preferences[waterKey] = newWater }

    val todayDateInt = getTodayAsInt()
    val lastDate = _dataStore.data.first()[_lastCompletedDateKey] ?: 0
    val streak = _dataStore.data.first()[_streakKey] ?: 0

    if (newWater < dailyGoal && lastDate == todayDateInt && streak > 0) {
      updateStreak(streak - 1)
      updateLastCompletedDate(0)
    }
  }

  fun getTodayAsInt(): Int {
    val today = LocalDate.now()
    return today.year * 10000 + today.monthValue * 100 + today.dayOfMonth
  }
}
