package com.bek.waterreminder.ui.screens.home.managewater.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bek.waterreminder.R
import com.bek.waterreminder.data.local.dataStore
import com.bek.waterreminder.ui.theme.Gilroy
import com.bek.waterreminder.viewmodel.ManageWaterViewModel
import com.bek.waterreminder.viewmodel.ManageWaterViewModelFactory

@Composable
fun WaterIntakeCard(percent: Float) {
  val context = LocalContext.current
  val dataStore = context.dataStore
  val viewModel: ManageWaterViewModel =
      viewModel(
          factory = ManageWaterViewModelFactory(dataStore),
      )

  val dailyGoal by viewModel.dailyGoalFlow.collectAsState(0)

  Box(
      modifier =
          Modifier.fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(Color.White, shape = RoundedCornerShape(8.dp))
              .border(1.dp, color = Color(0xffdee8f5), shape = RoundedCornerShape(8.dp))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
            "Your daily water intake",
            style =
                TextStyle(
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = colorResource(R.color.primary_black),
                ),
        )
        ChangeButton({})
      }
      Spacer(modifier = Modifier.height(8.dp))
      Box(
          modifier =
              Modifier.fillMaxWidth()
                  .clip(RoundedCornerShape(4.dp))
                  .border(
                      width = 1.dp,
                      color = colorResource(R.color.primary_blue),
                      shape = RoundedCornerShape(4.dp),
                  )
                  .background(Color(0xffeefaff), shape = RoundedCornerShape(4.dp))
                  .height(46.dp)
      ) {
        Box(
            modifier =
                Modifier.fillMaxHeight()
                    .fillMaxWidth(percent)
                    .clip(
                        shape =
                            RoundedCornerShape(
                                topStart = 4.dp,
                                bottomStart = 4.dp,
                                topEnd = if (percent >= 1f) 4.dp else 16.dp,
                                bottomEnd = if (percent >= 1f) 4.dp else 16.dp,
                            )
                    )
                    .background(
                        brush =
                            Brush.linearGradient(
                                colors =
                                    listOf(
                                        Color(0xff37c8ff),
                                        Color(0xff0196fa),
                                        Color(0xff0074e1),
                                        Color(0xff014eb6),
                                    )
                            )
                    ),
            contentAlignment = Alignment.Center,
        ) {
          if (percent > 0.1) {
            Text(
                "${(dailyGoal * percent).toInt()}ml",
                style =
                    TextStyle(
                        fontFamily = Gilroy,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    ),
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(8.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("0ml", style = progressTextStyle.copy(color = colorResource(R.color.primary_red)))
        Text(
            "${dailyGoal/2}ml",
            style = progressTextStyle.copy(color = colorResource(R.color.primary_blue)),
        )
        Text(
            "${dailyGoal}ml",
            style = progressTextStyle.copy(color = colorResource(R.color.primary_green)),
        )
      }
    }
  }
}

val progressTextStyle =
    TextStyle(
        fontFamily = Gilroy,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
    )

@Composable
fun ChangeButton(onClick: () -> Unit) {
  Box(
      modifier =
          Modifier.height(24.dp)
              .clip(RoundedCornerShape(8.35.dp))
              .background(color = Color(0xffe4f2fc), shape = RoundedCornerShape(8.35.dp))
              .clickable { onClick() },
      contentAlignment = Alignment.Center,
  ) {
    Text(
        "Change",
        modifier = Modifier.padding(horizontal = 11.dp),
        style =
            TextStyle(
                fontFamily = Gilroy,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = colorResource(R.color.primary_blue),
            ),
    )
  }
}
