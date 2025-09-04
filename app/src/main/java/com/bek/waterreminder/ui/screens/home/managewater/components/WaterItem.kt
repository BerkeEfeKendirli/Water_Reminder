package com.bek.waterreminder.ui.screens.home.managewater.components

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bek.waterreminder.R
import com.bek.waterreminder.ui.theme.Gilroy

@Composable
fun WaterItem(qty: Int, time: String, onDelete: () -> Unit) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .background(Color.White, shape = RoundedCornerShape(10.dp))
              .clip(shape = RoundedCornerShape(10.dp))
              .border(1.dp, color = Color(0xffdee8f5), shape = RoundedCornerShape(10.dp))
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
          buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorResource(R.color.primary_black))) {
              append("${qty}ml of water ")
            }
            withStyle(
                style =
                    SpanStyle(
                        color = colorResource(R.color.primary_blue),
                        fontStyle = FontStyle.Italic,
                    )
            ) {
              append("at $time")
            }
          },
          fontFamily = Gilroy,
          fontWeight = FontWeight.Medium,
          fontSize = 14.sp,
      )
      Image(
          painter = painterResource(R.drawable.close_circle),
          contentDescription = "Delete",
          colorFilter = ColorFilter.tint(colorResource(R.color.primary_red)),
          modifier = Modifier.clickable { onDelete() },
      )
    }
  }
}
