package com.bek.waterreminder.ui.screens.home.activity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bek.waterreminder.R
import com.bek.waterreminder.ui.theme.Gilroy

@Composable
fun WeeklyWaterBarChart(
    dayLabels: List<String>,
    weeklyWaterIntake: List<Int>,
    maxGoal: Int = 3000,
) {
  val maxWater = maxGoal.coerceAtLeast(weeklyWaterIntake.maxOrNull() ?: 0)

  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(250.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color.White, shape = RoundedCornerShape(8.dp))
              .border(1.dp, color = Color(0xffdee8f5), shape = RoundedCornerShape(8.dp))
  ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
      Text(
          "Water Intake Over the Last 7 Days",
          style =
              TextStyle(
                  fontFamily = Gilroy,
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  color = colorResource(R.color.primary_black),
              ),
          modifier = Modifier.padding(bottom = 8.dp),
      )
      Spacer(Modifier.height(2.dp))

      Row(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.Bottom,
          horizontalArrangement = Arrangement.SpaceAround,
      ) {
        weeklyWaterIntake.forEachIndexed { idx, water ->
          Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Bottom,
              modifier = Modifier.weight(1f),
          ) {
            // Bar container
            Box(
                modifier =
                    Modifier.height(140.dp) // sabit alan
                        .width(24.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
              Box(
                  modifier =
                      Modifier.fillMaxHeight(water / maxWater.toFloat())
                          .fillMaxWidth()
                          .clip(RoundedCornerShape(6.dp))
                          .background(
                              Brush.linearGradient(
                                  colors =
                                      listOf(
                                              Color(0xff37c8ff),
                                              Color(0xff0196fa),
                                              Color(0xff0074e1),
                                              Color(0xff014eb6),
                                          )
                                          .reversed()
                              )
                          )
              )
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = "${water}ml",
                style =
                    TextStyle(
                        fontFamily = Gilroy,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = colorResource(R.color.primary_black),
                    ),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = dayLabels.getOrNull(idx) ?: "",
                style =
                    TextStyle(
                        fontFamily = Gilroy,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = colorResource(R.color.primary_black),
                    ),
            )
          }
        }
      }
    }
  }
}
