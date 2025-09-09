package com.bek.waterreminder.ui.screens.home.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bek.waterreminder.data.local.dataStore
import com.bek.waterreminder.ui.screens.home.activity.components.WeeklyWaterBarChart
import com.bek.waterreminder.util.getLast7DaysLabels
import com.bek.waterreminder.viewmodel.ManageWaterViewModel
import com.bek.waterreminder.viewmodel.ManageWaterViewModelFactory

@Composable
fun ActivityScreen() {
  Column(
      modifier =
          Modifier.fillMaxSize()
              .background(color = Color(0xfff8f8ff))
              .padding(vertical = 24.dp, horizontal = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    val context = LocalContext.current
    val dataStore = context.dataStore
    val viewModel: ManageWaterViewModel =
        viewModel(
            factory = ManageWaterViewModelFactory(dataStore),
        )

    val dayLabels = getLast7DaysLabels()
    val weeklyWaterIntake by viewModel.weeklyWaterFlow.collectAsState(List(7) { 0 })

    WeeklyWaterBarChart(
        dayLabels = dayLabels,
        weeklyWaterIntake = weeklyWaterIntake,
        maxGoal = 2500,
    )
  }
}
