package com.bek.waterreminder.ui.screens.home.activity.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bek.waterreminder.R
import com.bek.waterreminder.ui.theme.Gilroy
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun WeeklyCompletionPercentageChart(
    percentage: Float,
    average: Float,
    strokeWidth: Dp = 30.dp,
) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .fillMaxHeight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(Color.White, shape = RoundedCornerShape(8.dp))
              .border(1.dp, color = Color(0xffdee8f5), shape = RoundedCornerShape(8.dp)),
  ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
          "Completion Rate Over the Last 7 Days",
          style =
              TextStyle(
                  fontFamily = Gilroy,
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  color = colorResource(R.color.primary_black),
              ),
      )
      Box(contentAlignment = Alignment.Center, modifier = Modifier.size(240.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val diameter = min(size.width, size.height)
          val arcSize = Size(diameter, diameter)
          val sweepAngle = percentage * 360f

          drawArc(
              color = Color(0xFF8C9197),
              startAngle = -90f,
              sweepAngle = 360f,
              useCenter = false,
              style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
              size = arcSize,
              topLeft = Offset(0f, 0f),
          )

          drawArc(
              brush =
                  Brush.linearGradient(
                      listOf(
                          Color(0xff37c8ff),
                          Color(0xff0196fa),
                          Color(0xff0074e1),
                          Color(0xff014eb6),
                      )
                  ),
              startAngle = -90f,
              sweepAngle = sweepAngle,
              useCenter = false,
              style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
              size = arcSize,
              topLeft = Offset(0f, 0f),
          )
        }

        Text(
            text = "${(percentage * 100).toInt()}%",
            style =
                TextStyle(
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = colorResource(R.color.primary_blue),
                ),
        )
      }
      Text(
          buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorResource(R.color.primary_black))) {
              append("Last 7 Days Average: ")
            }
            withStyle(
                style =
                    SpanStyle(
                        color = colorResource(R.color.primary_blue),
                        fontWeight = FontWeight.Bold,
                    )
            ) {
              append("${average.roundToInt()}ml")
            }
          },
          fontFamily = Gilroy,
          fontWeight = FontWeight.Medium,
          fontSize = 16.sp,
      )
    }
  }
}
