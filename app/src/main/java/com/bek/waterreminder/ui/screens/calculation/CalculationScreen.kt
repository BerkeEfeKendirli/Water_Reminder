package com.bek.waterreminder.ui.screens.calculation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.bek.waterreminder.R
import com.bek.waterreminder.data.local.dataStore
import com.bek.waterreminder.ui.components.CustomButton
import com.bek.waterreminder.ui.screens.calculation.components.WeightSlider
import com.bek.waterreminder.ui.theme.Gilroy
import com.bek.waterreminder.viewmodel.CalculationViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun CalculationScreen(viewModel: CalculationViewModel, onNavigateToHome: () -> Unit) {
  val viewModel = viewModel
  var intPart by remember { mutableStateOf(70) }
  var decimalPart by remember { mutableStateOf(0) }
  val weight = intPart + decimalPart / 10f
  val dailyGoal = (weight * 35).roundToInt()
  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  Column(
      modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 32.dp),
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
        buildAnnotatedString {
          withStyle(style = SpanStyle(color = colorResource(R.color.primary_black))) {
            append("You need to drink ")
          }
          withStyle(style = SpanStyle(color = colorResource(R.color.primary_blue))) {
            append("$dailyGoal" + "mL ")
          }
          withStyle(style = SpanStyle(color = colorResource(R.color.primary_black))) {
            append("water daily.")
          }
        },
        style =
            TextStyle(
                fontFamily = Gilroy,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = colorResource(R.color.primary_blue),
                textAlign = TextAlign.Center,
            ),
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
          "$weight" + "KG",
          style =
              TextStyle(
                  fontFamily = Gilroy,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 24.sp,
                  color = colorResource(R.color.primary_black),
              ),
      )
      Spacer(modifier = Modifier.height(50.dp))
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
      ) {
        WeightSlider(
            value = intPart,
            onValueChange = { intPart = it },
            valueRange = 40..150,
            steps = 110,
        )
        Spacer(modifier = Modifier.width(24.dp))
        WeightSlider(
            value = decimalPart,
            onValueChange = { decimalPart = it },
            valueRange = 0..9,
            steps = 9,
        )
      }
    }
    CustomButton(
        text = "Start",
        onClick = {
          scope.launch {
            context.dataStore.edit { prefs ->
              prefs[floatPreferencesKey("user_weight")] = weight
              prefs[intPreferencesKey("daily_goal")] = dailyGoal
              prefs[booleanPreferencesKey("has_set_weight")] = true
            }
          }
          onNavigateToHome()
        },
        modifier = Modifier.fillMaxWidth(),
    )
  }
}
