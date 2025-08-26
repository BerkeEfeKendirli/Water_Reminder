package com.bek.waterreminder.ui.screens.calculation.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.bek.waterreminder.R

@Composable
fun WeightSlider(
    value: Int,
    onValueChange: (Int) -> Unit,
    valueRange: IntRange,
    steps: Int = 0,
) {
  Slider(
      modifier = Modifier.width(200.dp),
      value = value.toFloat(),
      onValueChange = { onValueChange(it.toInt()) },
      valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
      steps = steps,
      colors =
          SliderDefaults.colors(
              thumbColor = colorResource(R.color.primary_blue),
              activeTrackColor = colorResource(R.color.primary_blue),
              activeTickColor = Color(0xFF1359A6),
              inactiveTrackColor = Color(0xFFACCAEA),
              inactiveTickColor = Color(0xFF7196BD),
          ),
  )
}
