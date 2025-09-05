package com.bek.waterreminder.ui.screens.home.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bek.waterreminder.R
import com.bek.waterreminder.ui.theme.Gilroy

@Composable
fun TimePickerDropdown(
    label: String,
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
) {
  Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Text(
        label,
        style =
            TextStyle(
                fontFamily = Gilroy,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = colorResource(R.color.primary_black),
            ),
    )
    var hourExpanded by remember { mutableStateOf(false) }
    Box(
        modifier =
            Modifier.width(60.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .border(
                    1.dp,
                    color = colorResource(R.color.primary_blue),
                    shape = RoundedCornerShape(4.dp),
                )
                .background(color = Color(0xffdee8f5), shape = RoundedCornerShape(4.dp))
                .clickable { hourExpanded = true },
        contentAlignment = Alignment.Center,
    ) {
      Text(
          "${hour.toString().padStart(2, '0')}h",
          modifier = Modifier.padding(8.dp),
          style =
              TextStyle(
                  fontFamily = Gilroy,
                  fontWeight = FontWeight.Medium,
                  fontSize = 14.sp,
                  color = colorResource(R.color.black),
                  textAlign = TextAlign.Center,
              ),
      )
      DropdownMenu(expanded = hourExpanded, onDismissRequest = { hourExpanded = false }) {
        (0..23).forEach { h ->
          DropdownMenuItem(
              onClick = {
                onHourChange(h)
                hourExpanded = false
              },
              text = {
                Text(
                    h.toString().padStart(2, '0'),
                    style =
                        TextStyle(
                            fontFamily = Gilroy,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = colorResource(R.color.black),
                            textAlign = TextAlign.Center,
                        ),
                )
              },
          )
        }
      }
    }
    var minuteExpanded by remember { mutableStateOf(false) }
    Box(
        modifier =
            Modifier.width(60.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .border(
                    1.dp,
                    color = colorResource(R.color.primary_blue),
                    shape = RoundedCornerShape(4.dp),
                )
                .background(color = Color(0xffdee8f5), shape = RoundedCornerShape(4.dp))
                .clickable { minuteExpanded = true },
        contentAlignment = Alignment.Center,
    ) {
      Text(
          "${minute.toString().padStart(2, '0')}m",
          modifier = Modifier.padding(8.dp),
          style =
              TextStyle(
                  fontFamily = Gilroy,
                  fontWeight = FontWeight.Medium,
                  fontSize = 14.sp,
                  color = colorResource(R.color.black),
                  textAlign = TextAlign.Center,
              ),
      )
      DropdownMenu(expanded = minuteExpanded, onDismissRequest = { minuteExpanded = false }) {
        (0..59 step 5).forEach { m ->
          DropdownMenuItem(
              onClick = {
                onMinuteChange(m)
                minuteExpanded = false
              },
              text = {
                Text(
                    m.toString().padStart(2, '0'),
                    style =
                        TextStyle(
                            fontFamily = Gilroy,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = colorResource(R.color.black),
                            textAlign = TextAlign.Center,
                        ),
                )
              },
          )
        }
      }
    }
  }
}
