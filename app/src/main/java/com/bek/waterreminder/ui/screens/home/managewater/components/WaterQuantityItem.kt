package com.bek.waterreminder.ui.screens.home.managewater.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bek.waterreminder.R
import com.bek.waterreminder.ui.theme.Gilroy

@Composable
fun WaterQuantityItem(isSelected: Boolean, quantity: Int, onClick: () -> Unit) {
  Box(
      contentAlignment = Alignment.Center,
      modifier =
          Modifier.clickable { onClick() }
              .clip(RoundedCornerShape(4.dp))
              .border(
                  1.dp,
                  color = colorResource(R.color.primary_blue),
                  shape = RoundedCornerShape(4.dp),
              )
              .background(
                  color =
                      if (isSelected) colorResource(R.color.primary_blue) else Color(0xfffefeff),
                  shape = RoundedCornerShape(4.dp),
              ),
  ) {
    Text(
        "${quantity}ml",
        style =
            TextStyle(
                fontFamily = Gilroy,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp,
                color = if (!isSelected) colorResource(R.color.primary_black) else Color.White,
            ),
        modifier = Modifier.padding(18.dp),
    )
  }
}
