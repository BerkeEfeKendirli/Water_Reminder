package com.bek.waterreminder.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate

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
  val lastCompletedDateFlow: Flow<Int> =
      _dataStore.data.map { preferences -> preferences[_lastCompletedDateKey] ?: 0 }

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

  suspend fun getAllDailyWaterData(): Map<Int, Int> {
    val prefs = _dataStore.data.first()
    return prefs
        .asMap()
        .filterKeys { it.name.startsWith("water_") }
        .mapKeys { it.key.name.removePrefix("water_").toInt() }
        .mapValues { it.value as Int }
  }

  fun getTodayAsInt(): Int {
    val today = LocalDate.now()
    return today.year * 10000 + today.monthValue * 100 + today.dayOfMonth
  }
}
