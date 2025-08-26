package com.bek.waterreminder.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculationViewModel : ViewModel() {
  private val _dailyWater = MutableStateFlow(0)
  val dailyWater = _dailyWater.asStateFlow()

  fun setDailyWater(amount: Int) {
    _dailyWater.value = amount
  }
}
