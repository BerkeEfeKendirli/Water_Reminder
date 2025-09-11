package com.bek.waterreminder.ui.screens.home.managewater.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AnimatedWaterItem(qty: Int, time: String, onDelete: () -> Unit) {
  var visible by remember { mutableStateOf(true) }

  AnimatedVisibility(
      visible = visible,
      exit = fadeOut() + shrinkVertically() + scaleOut(),
      label = "waterItem",
  ) {
    WaterItem(qty = qty, time = time, onDelete = { visible = false })
  }

  if (!visible) {
    LaunchedEffect(Unit) {
      kotlinx.coroutines.delay(300)
      onDelete()
    }
  }
}
