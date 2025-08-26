package com.bek.waterreminder.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bek.waterreminder.viewmodel.CalculationViewModel

@Composable
fun HomeScreen(viewModel: CalculationViewModel) {
  val viewModel = viewModel
  val dailyWater by viewModel.dailyWater.collectAsState()
  Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text("$dailyWater")
  }
}
