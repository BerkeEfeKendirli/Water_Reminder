package com.bek.waterreminder.ui.screens.home.managewater.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bek.waterreminder.R
import com.bek.waterreminder.ui.theme.Gilroy
import java.time.LocalTime

@Composable
fun TimeGreetingCard() {
  val hour = LocalTime.now().hour
  val (greeting, iconRes, description) = getGreetingIconAndDescription(hour)

  Row(
      modifier =
          Modifier.fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(Color.White, shape = RoundedCornerShape(8.dp))
              .border(1.dp, color = Color(0xffdee8f5), shape = RoundedCornerShape(8.dp)),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      Image(
          painter = painterResource(R.drawable.camel),
          contentDescription = null,
          Modifier.size(36.dp),
      )
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
            greeting,
            style =
                TextStyle(
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = colorResource(R.color.primary_black),
                ),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            description,
            style =
                TextStyle(
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = colorResource(R.color.primary_black),
                ),
        )
      }
    }
    Image(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = Modifier.size(68.dp),
    )
  }
}

fun getGreetingIconAndDescription(hour: Int): Triple<String, Int, String> {
  return when (hour) {
    in 5..11 -> Triple("Good morning", R.drawable.morning, "It's a bright new day!!")
    in 12..16 ->
        Triple("Good afternoon", R.drawable.afternoon, "Keep shining through the afternoon!")
    in 17..21 -> Triple("Good evening", R.drawable.evening, "Hope you had a productive day.")
    else -> Triple("Good night", R.drawable.night, "Rest well and recharge for tomorrow.")
  }
}
