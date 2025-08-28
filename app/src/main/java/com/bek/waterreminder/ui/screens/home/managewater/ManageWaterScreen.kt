package com.bek.waterreminder.ui.screens.home.managewater

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bek.waterreminder.ui.components.CustomButton
import com.bek.waterreminder.ui.screens.home.managewater.components.StreakCard
import com.bek.waterreminder.ui.screens.home.managewater.components.TimeGreetingCard
import com.bek.waterreminder.ui.screens.home.managewater.components.WaterIntakeCard

@Composable
fun ManageWaterScreen() {
  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(vertical = 24.dp, horizontal = 16.dp)
              .background(color = Color(0xfff8f8ff)),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    TimeGreetingCard()
    Spacer(modifier = Modifier.height(8.dp))
    WaterIntakeCard(percent = 0.93f)
    Spacer(modifier = Modifier.height(8.dp))
    StreakCard(hasStreak = true, streakCount = 948)
    Spacer(modifier = Modifier.weight(1f))
    CustomButton(onClick = {}, text = "+ Drink Water", modifier = Modifier.fillMaxWidth())
  }
}
