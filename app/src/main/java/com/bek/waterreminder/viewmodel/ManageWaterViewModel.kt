package com.bek.waterreminder.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bek.waterreminder.data.model.managewater.WaterEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class ManageWaterViewModel(private val _dataStore: DataStore<Preferences>) : ViewModel() {

  private val _dailyGoalKey = intPreferencesKey("daily_goal")
  private val _streakKey = intPreferencesKey("streak_count")
  private val _lastCompletedDateKey = intPreferencesKey("last_completed_date")
  private val _weightKey = floatPreferencesKey("user_weight")
  private val _selectedCupKey = intPreferencesKey("selected_cup")

  init {
    viewModelScope.launch {
      restoreStreak()
      migrateExistingEntries()
    }
  }

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

  val dailyPercentFlow =
      combine(dailyWaterFlow, dailyGoalFlow) { water, goal ->
        if (goal > 0) water.toFloat() / goal.toFloat() else 0f
      }

  val todayWaterEntriesFlow: Flow<List<WaterEntry>> =
      _dataStore.data.map { preferences ->
        val key = getWaterEntriesKeyForToday()
        val json = preferences[key] ?: "[]"
        try {
          Json.decodeFromString<List<WaterEntry>>(json)
        } catch (e: Exception) {
          emptyList()
        }
      }

  val weeklyWaterFlow: Flow<List<Int>> =
      _dataStore.data.map { preferences ->
        (0..6).map { offset ->
          val date = LocalDate.now().minusDays((6 - offset).toLong())
          val key = intPreferencesKey("water_${getDateAsInt(date)}")
          preferences[key] ?: 0
        }
      }

  val weeklyAverageFlow: Flow<Float> =
      weeklyWaterFlow.map { weeklyList ->
        if (weeklyList.isEmpty()) 0f else weeklyList.average().toFloat()
      }

  val weeklyCompletionRateFlow: Flow<Float> =
      weeklyWaterFlow.combine(dailyGoalFlow) { weeklyList, goal ->
        if (goal == 0 || weeklyList.isEmpty()) return@combine 0f
        val completedDays = weeklyList.count { it >= goal }
        completedDays / 7f
      }

  suspend fun removeWaterEntryById(id: String) {
    val key = getWaterEntriesKeyForToday()
    val currentJson = _dataStore.data.first()[key] ?: "[]"
    val currentList = Json.decodeFromString<List<WaterEntry>>(currentJson)

    val entryToRemove = currentList.find { it.id == id }
    if (entryToRemove != null) {
      val newList = currentList.filter { it.id != id }
      _dataStore.edit { it[key] = Json.encodeToString(newList) }
      decreaseWater(entryToRemove.amount)
    }
  }

  suspend fun addWaterEntryToday(amount: Int) {
    val key = getWaterEntriesKeyForToday()
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("hh:mma")
    val formattedTime = currentTime.format(formatter)

    val currentJson = _dataStore.data.first()[key] ?: "[]"
    val currentList = Json.decodeFromString<List<WaterEntry>>(currentJson)

    val newEntry =
        WaterEntry(id = UUID.randomUUID().toString(), amount = amount, time = formattedTime)
    val newList = currentList + newEntry

    _dataStore.edit { it[key] = Json.encodeToString(newList) }
    drinkWater(amount)
  }

  suspend fun removeWaterEntryToday(index: Int) {
    val key = getWaterEntriesKeyForToday()
    val currentJson = _dataStore.data.first()[key] ?: "[]"
    val currentList = Json.decodeFromString<List<WaterEntry>>(currentJson)

    if (index in currentList.indices) {
      val removedEntry = currentList[index]
      val newList = currentList.toMutableList().apply { removeAt(index) }

      _dataStore.edit { it[key] = Json.encodeToString(newList) }
      decreaseWater(removedEntry.amount)
    }
  }

  private suspend fun migrateExistingEntries() {
    val key = getWaterEntriesKeyForToday()
    val currentJson = _dataStore.data.first()[key] ?: "[]"

    try {
      val currentList = Json.decodeFromString<List<WaterEntry>>(currentJson)
      val needsMigration =
          currentList.any {
            try {

              it.id.isEmpty()
            } catch (e: Exception) {
              true
            }
          }

      if (needsMigration) {
        val migratedList =
            currentList.map { entry ->
              if (entry.id.isEmpty()) {
                entry.copy(id = UUID.randomUUID().toString())
              } else {
                entry
              }
            }
        _dataStore.edit { it[key] = Json.encodeToString(migratedList) }
      }
    } catch (e: Exception) {
      _dataStore.edit { it[key] = "[]" }
    }
  }

  private suspend fun restoreStreak() {
    val prefs = _dataStore.data.first()
    val dailyGoal = prefs[_dailyGoalKey] ?: 2000
    val lastDateInt = prefs[_lastCompletedDateKey] ?: 0
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    val todayWater = prefs[getWaterKeyForToday()] ?: 0
    val streak = prefs[_streakKey] ?: 0

    if (lastDateInt != 0) {
      val lastCompletedDate =
          LocalDate.of(lastDateInt / 10000, (lastDateInt % 10000) / 100, lastDateInt % 100)

      if (lastCompletedDate.isBefore(yesterday)) {
        updateStreak(0)
      }
    }

    val lastDateAfterReset = _dataStore.data.first()[_lastCompletedDateKey] ?: 0
    if (todayWater >= dailyGoal && lastDateAfterReset != getTodayAsInt()) {
      updateStreak(streak + 1)
      updateLastCompletedDate(getTodayAsInt())
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

  suspend fun drinkWater(amount: Int) {
    val prefs = _dataStore.data.first()
    val waterKey = getWaterKeyForToday()
    val currentWater = prefs[waterKey] ?: 0
    val dailyGoal = prefs[_dailyGoalKey] ?: 2000

    val newWater = currentWater + amount
    _dataStore.edit { preferences -> preferences[waterKey] = newWater }

    val todayDateInt = getTodayAsInt()
    val lastDate = prefs[_lastCompletedDateKey] ?: 0
    val streak = prefs[_streakKey] ?: 0

    if (newWater >= dailyGoal && lastDate != todayDateInt) {
      updateStreak(streak + 1)
      updateLastCompletedDate(todayDateInt)
    }
  }

  suspend fun decreaseWater(amount: Int) {
    val prefs = _dataStore.data.first()
    val waterKey = getWaterKeyForToday()
    val currentWater = prefs[waterKey] ?: 0
    val dailyGoal = prefs[_dailyGoalKey] ?: 2000

    val newWater = (currentWater - amount).coerceAtLeast(0)
    _dataStore.edit { preferences -> preferences[waterKey] = newWater }

    val todayDateInt = getTodayAsInt()
    val lastDate = prefs[_lastCompletedDateKey] ?: 0
    val streak = prefs[_streakKey] ?: 0

    if (newWater < dailyGoal && lastDate == todayDateInt && streak > 0) {
      updateStreak(streak - 1)
      updateLastCompletedDate(0)
    }
  }

  private fun getDateAsInt(date: LocalDate): Int {
    return date.year * 10000 + date.monthValue * 100 + date.dayOfMonth
  }

  fun getTodayAsInt(): Int = getDateAsInt(LocalDate.now())

  fun getWaterKeyForToday(): Preferences.Key<Int> {
    return intPreferencesKey("water_${getTodayAsInt()}")
  }

  fun getWaterEntriesKeyForToday(): Preferences.Key<String> {
    return stringPreferencesKey("water_entries_${getTodayAsInt()}")
  }
}
