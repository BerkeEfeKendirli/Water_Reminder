package com.bek.waterreminder.ui.screens.home.settings

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bek.waterreminder.R
import com.bek.waterreminder.data.local.dataStore
import com.bek.waterreminder.ui.components.CustomButton
import com.bek.waterreminder.ui.screens.calculation.components.WeightSlider
import com.bek.waterreminder.ui.screens.home.settings.components.SettingsButton
import com.bek.waterreminder.ui.screens.home.settings.components.TimePickerDropdown
import com.bek.waterreminder.ui.screens.home.settings.components.WaterQuantityCard
import com.bek.waterreminder.ui.theme.Gilroy
import com.bek.waterreminder.util.updateNotificationWorker
import com.bek.waterreminder.viewmodel.ManageWaterViewModel
import com.bek.waterreminder.viewmodel.ManageWaterViewModelFactory
import com.bek.waterreminder.viewmodel.SettingsViewModel
import com.bek.waterreminder.viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
  val context = LocalContext.current
  val dataStore = context.dataStore
  val viewModel: ManageWaterViewModel =
      viewModel(
          factory = ManageWaterViewModelFactory(dataStore),
      )
  val settingsViewModel: SettingsViewModel =
      viewModel(factory = SettingsViewModelFactory(dataStore))
  val coroutineScope = rememberCoroutineScope()
  val sheetState = rememberModalBottomSheetState()
  var showGoalSheet by remember { mutableStateOf(false) }
  var showTimerSheet by remember { mutableStateOf(false) }
  var lPart by remember { mutableIntStateOf(2) }
  var mlPart by remember { mutableIntStateOf(500) }
  val updatedGoal = lPart.times(1000) + mlPart
  val userWeight by viewModel.weightFlow.collectAsState(0f)

  val sleepStartHour by settingsViewModel.sleepStartHour.collectAsState(initial = 23)
  val sleepStartMinute by settingsViewModel.sleepStartMinute.collectAsState(initial = 0)
  val sleepEndHour by settingsViewModel.sleepEndHour.collectAsState(initial = 7)
  val sleepEndMinute by settingsViewModel.sleepEndMinute.collectAsState(initial = 0)
  val notificationIntervalMinutes by
      settingsViewModel.notificationIntervalMinutes.collectAsState(initial = 60L)

  val notificationHour = (notificationIntervalMinutes / 60).toInt()
  val notificationMinute = (notificationIntervalMinutes % 60).toInt()

  var sleepStartHourLocal by remember { mutableIntStateOf(sleepStartHour) }
  var sleepStartMinuteLocal by remember { mutableIntStateOf(sleepStartMinute) }
  var sleepEndHourLocal by remember { mutableIntStateOf(sleepEndHour) }
  var sleepEndMinuteLocal by remember { mutableIntStateOf(sleepEndMinute) }
  var notificationHourLocal by remember { mutableIntStateOf(notificationHour) }
  var notificationMinuteLocal by remember { mutableIntStateOf(notificationMinute) }

  Column(
      modifier = Modifier.fillMaxSize().background(color = Color(0xfff8f8ff)).padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    SettingsButton(
        text = "Set your daily water intake goal",
        icon = R.drawable.baseline_arrow_forward_ios_24,
        onClick = { showGoalSheet = true },
    )
    SettingsButton(
        text = "Set sleep/notification timer",
        icon = R.drawable.baseline_arrow_forward_ios_24,
        onClick = { showTimerSheet = true },
    )
    WaterQuantityCard()
    SettingsButton(
        text = "Visit source code",
        icon = R.drawable.github_mark,
        onClick = {
          val intent =
              Intent(
                  Intent.ACTION_VIEW,
                  "https://github.com/BerkeEfeKendirli/Water_Reminder".toUri(),
              )
          context.startActivity(intent)
        },
    )
  }

  if (showGoalSheet) {
    ModalBottomSheet(onDismissRequest = { showGoalSheet = false }, sheetState = sheetState) {
      Column(modifier = Modifier.height(350.dp), verticalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
              "Set Goal",
              style =
                  TextStyle(
                      fontFamily = Gilroy,
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 16.sp,
                      color = Color(0xff413B89),
                  ),
          )
          Image(
              painter = painterResource(R.drawable.close_circle),
              contentDescription = "Close",
              modifier = Modifier.clickable { showGoalSheet = false },
          )
        }
        HorizontalDivider(thickness = 1.dp, color = Color(0xffd9cffb))
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
          Row(modifier = Modifier.padding(16.dp)) {
            WeightSlider(
                value = lPart,
                onValueChange = { lPart = it },
                valueRange = 0..9,
                steps = 9,
            )
            Spacer(modifier = Modifier.width(24.dp))
            WeightSlider(
                value = mlPart,
                onValueChange = {
                  val steps = listOf(0, 100, 200, 300, 400, 500, 600, 700, 800, 900)
                  val nearest = steps.minByOrNull { step -> kotlin.math.abs(step - it) } ?: it
                  mlPart = nearest
                },
                valueRange = 0..900,
                steps = 8,
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
              "${updatedGoal}ml",
              style =
                  TextStyle(
                      fontFamily = Gilroy,
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 36.sp,
                      color = colorResource(R.color.primary_blue),
                  ),
          )
        }
        Column {
          CustomButton(
              text = "Save Daily Goal",
              onClick = {
                coroutineScope.launch { viewModel.updateDailyGoal(updatedGoal) }
                showGoalSheet = false
                Toast.makeText(context, "Goal Saved", Toast.LENGTH_SHORT).show()
              },
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
          )
          Spacer(modifier = Modifier.height(8.dp))
          CustomButton(
              text = "Reset To Default",
              onClick = {
                val dailyGoal = userWeight * 35
                coroutineScope.launch { viewModel.updateDailyGoal(dailyGoal.toInt()) }
                showGoalSheet = false
                Toast.makeText(context, "Goal Reset To Default", Toast.LENGTH_SHORT).show()
              },
              modifier =
                  Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
          )
        }
      }
    }
  }

  //
  if (showTimerSheet) {
    ModalBottomSheet(onDismissRequest = { showTimerSheet = false }, sheetState = sheetState) {
      Column(modifier = Modifier.height(350.dp), verticalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
              "Set Sleep/Notification Timer",
              style =
                  TextStyle(
                      fontFamily = Gilroy,
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 16.sp,
                      color = Color(0xff413B89),
                  ),
          )
          Image(
              painter = painterResource(R.drawable.close_circle),
              contentDescription = "Close",
              modifier = Modifier.clickable { showTimerSheet = false },
          )
        }
        HorizontalDivider(thickness = 1.dp, color = Color(0xffd9cffb))
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Column(
                  modifier = Modifier.weight(1f),
                  horizontalAlignment = Alignment.Start,
                  verticalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                Text(
                    "Set Sleep Timer",
                    style =
                        TextStyle(
                            fontFamily = Gilroy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = colorResource(R.color.primary_black),
                        ),
                )
                TimePickerDropdown(
                    label = "Start:",
                    hour = sleepStartHourLocal,
                    minute = sleepStartMinuteLocal,
                    onHourChange = { sleepStartHourLocal = it },
                    onMinuteChange = { sleepStartMinuteLocal = it },
                )
                TimePickerDropdown(
                    label = "End:  ",
                    hour = sleepEndHourLocal,
                    minute = sleepEndMinuteLocal,
                    onHourChange = { sleepEndHourLocal = it },
                    onMinuteChange = { sleepEndMinuteLocal = it },
                )
              }
              Column(
                  modifier = Modifier.weight(1f),
                  horizontalAlignment = Alignment.Start,
                  verticalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                Text(
                    "Set Notification Timer",
                    style =
                        TextStyle(
                            fontFamily = Gilroy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = colorResource(R.color.primary_black),
                        ),
                )
                TimePickerDropdown(
                    label = "Interval:",
                    hour = notificationHourLocal,
                    minute = notificationMinuteLocal,
                    onHourChange = { notificationHourLocal = it },
                    onMinuteChange = { notificationMinuteLocal = it },
                )
              }
            }
          }
        }
        CustomButton(
            text = "Set Sleep/Notification Timer",
            onClick = {
              coroutineScope.launch {
                settingsViewModel.setSleepStart(sleepStartHourLocal, sleepStartMinuteLocal)
                settingsViewModel.setSleepEnd(sleepEndHourLocal, sleepEndMinuteLocal)
                settingsViewModel.setNotificationInterval(
                    notificationHourLocal,
                    notificationMinuteLocal,
                )
                updateNotificationWorker(
                    context,
                    (notificationHourLocal * 60 + notificationMinuteLocal).toLong(),
                )
              }
              showTimerSheet = false
              Toast.makeText(context, "Timer Set", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        )
      }
    }
  }
}
