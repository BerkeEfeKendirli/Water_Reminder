package com.bek.waterreminder.ui.screens.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.Black,
    inactiveColor: Color = Color(0xffb3b3b3),
) {
  Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
  ) {
    repeat(pageCount) { index ->
      Box(
          modifier =
              Modifier.size(4.dp)
                  .background(
                      color = if (index == currentPage) activeColor else inactiveColor,
                      shape = CircleShape,
                  )
      )
    }
  }
}
