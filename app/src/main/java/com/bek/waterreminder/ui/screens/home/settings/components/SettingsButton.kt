package com.bek.waterreminder.ui.screens.home.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
fun SettingsButton(text: String, onClick: () -> Unit, icon: Int) {
  Box(
      modifier =
          Modifier.clip(RoundedCornerShape(8.dp))
              .border(1.dp, shape = RoundedCornerShape(8.dp), color = Color(0xffdee8f5))
              .background(Color.White, shape = RoundedCornerShape(8.dp))
              .clickable { onClick() }
              .fillMaxWidth(),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
          text = text,
          style =
              TextStyle(
                  fontFamily = Gilroy,
                  fontWeight = FontWeight.Medium,
                  fontSize = 14.sp,
                  color = colorResource(R.color.primary_black),
              ),
      )
      Image(
          painter = painterResource(icon),
          contentDescription = "Forward",
          colorFilter = ColorFilter.tint(color = colorResource(R.color.primary_black)),
      )
    }
  }
}
