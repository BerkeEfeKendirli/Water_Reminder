package com.bek.waterreminder.ui.screens.home.managewater

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bek.waterreminder.data.local.dataStore
import com.bek.waterreminder.ui.components.CustomButton
import com.bek.waterreminder.ui.screens.home.managewater.components.StreakCard
import com.bek.waterreminder.ui.screens.home.managewater.components.TimeGreetingCard
import com.bek.waterreminder.ui.screens.home.managewater.components.WaterIntakeCard
import com.bek.waterreminder.viewmodel.ManageWaterViewModel
import com.bek.waterreminder.viewmodel.ManageWaterViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun ManageWaterScreen() {
  val context = LocalContext.current
  val dataStore = context.dataStore
  val viewModel: ManageWaterViewModel =
      viewModel(
          factory = ManageWaterViewModelFactory(dataStore),
      )
  val coroutineScope = rememberCoroutineScope()
  val dailyGoal by viewModel.dailyGoalFlow.collectAsState(0)
  val dailyWater by viewModel.dailyWaterFlow.collectAsState(0)
  val percentage = if (dailyGoal > 0) dailyWater.toFloat() / dailyGoal.toFloat() else 0f
  val streakCount by viewModel.streakFlow.collectAsState(0)
  val selectedCup by viewModel.selectedCupFlow.collectAsState(200)

  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(vertical = 24.dp, horizontal = 16.dp)
              .background(color = Color(0xfff8f8ff)),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    TimeGreetingCard()
    Spacer(modifier = Modifier.height(8.dp))
    WaterIntakeCard(percent = percentage)
    Spacer(modifier = Modifier.height(8.dp))
    StreakCard(hasStreak = streakCount > 0, streakCount = streakCount)
    Spacer(modifier = Modifier.weight(1f))
    CustomButton(
        onClick = {
          coroutineScope.launch { viewModel.drinkWater(amount = selectedCup) }
          Toast.makeText(context, "Added ${selectedCup}ml of water", Toast.LENGTH_SHORT).show()
        },
        text = "+ Drink Water (${selectedCup}ml)",
        modifier = Modifier.fillMaxWidth(),
    )
  }
}
