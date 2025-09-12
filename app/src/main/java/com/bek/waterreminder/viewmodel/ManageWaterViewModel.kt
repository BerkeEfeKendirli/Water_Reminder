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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
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

  private val _selectedDate = MutableStateFlow(LocalDate.now())
  val selectedDateFlow: StateFlow<LocalDate> = _selectedDate

  fun setSelectedDate(date: LocalDate) {
    _selectedDate.value = date
  }

  init {
    viewModelScope.launch {
      recalculateStreak()
      migrateExistingEntries()
    }
  }

  val selectedCupFlow: Flow<Int> =
      _dataStore.data.map { preferences -> preferences[_selectedCupKey] ?: 200 }

  val dailyGoalFlow: Flow<Int> =
      _dataStore.data.map { preferences -> preferences[_dailyGoalKey] ?: 2000 }

  val weightFlow: Flow<Float> = _dataStore.data.map { preferences -> preferences[_weightKey] ?: 0f }

  val streakFlow: Flow<Int> = _dataStore.data.map { preferences -> preferences[_streakKey] ?: 0 }

  @OptIn(ExperimentalCoroutinesApi::class)
  val selectedDayWaterFlow: Flow<Int> =
      selectedDateFlow.flatMapLatest { date ->
        _dataStore.data.map { preferences ->
          val key = getWaterKeyFor(date)
          preferences[key] ?: 0
        }
      }

  @OptIn(ExperimentalCoroutinesApi::class)
  val selectedDayWaterEntriesFlow: Flow<List<WaterEntry>> =
      selectedDateFlow.flatMapLatest { date ->
        _dataStore.data.map { preferences ->
          val key = getWaterEntriesKeyFor(date)
          val json = preferences[key] ?: "[]"
          try {
            Json.decodeFromString<List<WaterEntry>>(json)
          } catch (e: Exception) {
            emptyList()
          }
        }
      }

  val selectedDayPercentFlow: Flow<Float> =
      selectedDayWaterFlow.combine(dailyGoalFlow) { water, goal ->
        if (goal > 0) water.toFloat() / goal.toFloat() else 0f
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

  suspend fun addWaterEntry(amount: Int) {
    val date = selectedDateFlow.value
    val key = getWaterEntriesKeyFor(date)
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("hh:mma")
    val formattedTime = currentTime.format(formatter)

    val currentJson = _dataStore.data.first()[key] ?: "[]"
    val currentList = Json.decodeFromString<List<WaterEntry>>(currentJson)

    val newEntry =
        WaterEntry(id = UUID.randomUUID().toString(), amount = amount, time = formattedTime)
    val newList = currentList + newEntry

    _dataStore.edit { it[key] = Json.encodeToString(newList) }

    val waterKey = getWaterKeyFor(date)
    val prefs = _dataStore.data.first()
    val currentWater = prefs[waterKey] ?: 0
    val newWater = currentWater + amount
    _dataStore.edit { it[waterKey] = newWater }

    recalculateStreak()
  }

  suspend fun recalculateStreak() {
    val prefs = _dataStore.data.first()
    val dailyGoal = prefs[_dailyGoalKey] ?: 2000

    var streak = 0
    val day = LocalDate.now()
    var startDay = day

    val todayWater = prefs[getWaterKeyFor(day)] ?: 0
    if (todayWater < dailyGoal) {
      startDay = day.minusDays(1)
    }

    while (true) {
      val water = prefs[getWaterKeyFor(startDay)] ?: 0
      if (water >= dailyGoal) {
        streak += 1
        startDay = startDay.minusDays(1)
      } else {
        break
      }
    }
    updateStreak(streak)
  }

  suspend fun removeWaterEntryById(id: String) {
    val date = selectedDateFlow.value
    val key = getWaterEntriesKeyFor(date)
    val currentJson = _dataStore.data.first()[key] ?: "[]"
    val currentList = Json.decodeFromString<List<WaterEntry>>(currentJson)

    val entryToRemove = currentList.find { it.id == id }
    if (entryToRemove != null) {
      val newList = currentList.filter { it.id != id }
      _dataStore.edit { it[key] = Json.encodeToString(newList) }

      val waterKey = getWaterKeyFor(date)
      val prefs = _dataStore.data.first()
      val currentWater = prefs[waterKey] ?: 0
      val newWater = (currentWater - entryToRemove.amount).coerceAtLeast(0)
      _dataStore.edit { it[waterKey] = newWater }

      recalculateStreak()
    }
  }

  private fun getDateAsInt(date: LocalDate): Int {
    return date.year * 10000 + date.monthValue * 100 + date.dayOfMonth
  }

  fun getWaterKeyFor(date: LocalDate): Preferences.Key<Int> {
    return intPreferencesKey("water_${getDateAsInt(date)}")
  }

  fun getWaterEntriesKeyFor(date: LocalDate): Preferences.Key<String> {
    return stringPreferencesKey("water_entries_${getDateAsInt(date)}")
  }

  suspend fun updateWeight(weight: Float) {
    _dataStore.edit { preferences -> preferences[_weightKey] = weight }
  }

  suspend fun updateDailyGoal(newGoal: Int) {
    _dataStore.edit { preferences -> preferences[_dailyGoalKey] = newGoal }
    recalculateStreak()
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

  // MIGRATION VE STREAK LOGIC
  private suspend fun migrateExistingEntries() {
    val date = selectedDateFlow.value
    val key = getWaterEntriesKeyFor(date)
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
}
