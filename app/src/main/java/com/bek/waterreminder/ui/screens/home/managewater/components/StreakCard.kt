package com.bek.waterreminder.ui.screens.home.managewater.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bek.waterreminder.R
import com.bek.waterreminder.ui.theme.Gilroy

@Composable
fun StreakCard(hasStreak: Boolean, streakCount: Int = 0) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(color = Color.White, shape = RoundedCornerShape(8.dp))
              .border(
                  1.dp,
                  color =
                      if (hasStreak) colorResource(R.color.primary_green) else Color(0xffdee8f5),
                  shape = RoundedCornerShape(8.dp),
              )
              .padding(24.dp),
      contentAlignment = Alignment.Center,
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Image(
          painter = painterResource(R.drawable.cup_star),
          contentDescription = "Streak",
          colorFilter = if (!hasStreak) ColorFilter.tint(color = Color(0xFF8C9197)) else null,
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
          text = if (hasStreak) "Congratulations!!" else "Not on a streak!",
          style =
              TextStyle(
                  fontFamily = Gilroy,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 14.sp,
                  color = if (hasStreak) colorResource(R.color.primary_blue) else Color(0xFF8C9197),
              ),
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
          text =
              if (hasStreak) "You are on a ${streakCount}-day streak."
              else "Keep going to start your streak.",
          style =
              TextStyle(
                  fontFamily = Gilroy,
                  fontWeight = FontWeight.Medium,
                  fontSize = 14.sp,
                  color =
                      if (hasStreak) colorResource(R.color.primary_black) else Color(0xFF8C9197),
              ),
      )
    }
  }
}
