package com.bek.waterreminder.ui.screens.home.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bek.waterreminder.ui.screens.home.activity.components.WeeklyCompletionPercentageChart
import com.bek.waterreminder.ui.screens.home.activity.components.WeeklyWaterBarChart
import com.bek.waterreminder.util.getLast7DaysLabels
import com.bek.waterreminder.viewmodel.ManageWaterViewModel

@Composable
fun ActivityScreen(viewModel: ManageWaterViewModel) {
  Column(
      modifier =
          Modifier.fillMaxSize()
              .background(color = Color(0xfff8f8ff))
              .padding(vertical = 24.dp, horizontal = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    val dayLabels = getLast7DaysLabels()
    val weeklyWaterIntake by viewModel.weeklyWaterFlow.collectAsState(List(7) { 0 })
    val completionPercentage by viewModel.weeklyCompletionRateFlow.collectAsState(0f)
    val sevenDaysAverage by viewModel.weeklyAverageFlow.collectAsState(0f)

    WeeklyWaterBarChart(
        dayLabels = dayLabels,
        weeklyWaterIntake = weeklyWaterIntake,
        maxGoal = 2500,
    )
    Spacer(Modifier.height(8.dp))
    WeeklyCompletionPercentageChart(percentage = completionPercentage, average = sevenDaysAverage)
  }
}
