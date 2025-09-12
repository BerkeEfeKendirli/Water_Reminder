package com.bek.waterreminder.ui.screens.home.managewater

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bek.waterreminder.ui.components.CustomButton
import com.bek.waterreminder.ui.screens.home.managewater.components.AnimatedWaterItem
import com.bek.waterreminder.ui.screens.home.managewater.components.CalendarDialog
import com.bek.waterreminder.ui.screens.home.managewater.components.StreakCard
import com.bek.waterreminder.ui.screens.home.managewater.components.TimeGreetingCard
import com.bek.waterreminder.ui.screens.home.managewater.components.WaterIntakeCard
import com.bek.waterreminder.viewmodel.ManageWaterViewModel
import kotlinx.coroutines.launch

@Composable
fun ManageWaterScreen(
    showCalendar: Boolean,
    onCloseCalendar: () -> Unit,
    viewModel: ManageWaterViewModel,
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val percentage by viewModel.selectedDayPercentFlow.collectAsState(0f)
  val streakCount by viewModel.streakFlow.collectAsState(0)
  val selectedCup by viewModel.selectedCupFlow.collectAsState(200)
  val todayWaterEntries by viewModel.selectedDayWaterEntriesFlow.collectAsState(emptyList())

  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(vertical = 24.dp, horizontal = 16.dp)
              .background(color = Color(0xfff8f8ff)),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    TimeGreetingCard()
    Spacer(modifier = Modifier.height(8.dp))
    WaterIntakeCard(percent = percentage, viewModel = viewModel)
    Spacer(modifier = Modifier.height(8.dp))
    StreakCard(hasStreak = streakCount > 0, streakCount = streakCount)
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn(modifier = Modifier.weight(1f)) {
      itemsIndexed(
          items = todayWaterEntries.reversed(),
          key = { _, entry -> entry.id },
      ) { _, entry ->
        AnimatedWaterItem(
            qty = entry.amount,
            time = entry.time,
            onDelete = { coroutineScope.launch { viewModel.removeWaterEntryById(entry.id) } },
        )
        Spacer(modifier = Modifier.height(8.dp))
      }
    }

    CustomButton(
        onClick = {
          coroutineScope.launch { viewModel.addWaterEntry(selectedCup) }
          Toast.makeText(context, "Added ${selectedCup}ml of water", Toast.LENGTH_SHORT).show()
        },
        text = "+ Drink Water (${selectedCup}ml)",
        modifier = Modifier.fillMaxWidth(),
    )
  }
  CalendarDialog(
      show = showCalendar,
      onClose = onCloseCalendar,
      onDateSelected = { date -> viewModel.setSelectedDate(date) },
  )
}
