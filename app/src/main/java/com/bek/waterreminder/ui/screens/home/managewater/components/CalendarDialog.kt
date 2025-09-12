package com.bek.waterreminder.ui.screens.home.managewater.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bek.waterreminder.R
import com.bek.waterreminder.ui.theme.Gilroy
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(show: Boolean, onClose: () -> Unit, onDateSelected: (LocalDate) -> Unit) {
  val today = LocalDate.now()
  val tomorrowMillis =
      today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

  val datePickerState =
      rememberDatePickerState(
          selectableDates =
              object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                  return utcTimeMillis < tomorrowMillis
                }
              }
      )

  if (show) {
    DatePickerDialog(
        onDismissRequest = onClose,
        confirmButton = {
          TextButton(
              onClick = {
                val millis = datePickerState.selectedDateMillis
                if (millis != null) {
                  val date =
                      Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                  onDateSelected(date)
                }
                onClose()
              }
          ) {
            Text(
                "OK",
                style =
                    TextStyle(
                        fontFamily = Gilroy,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = colorResource(R.color.primary_blue),
                    ),
            )
          }
        },
        dismissButton = {
          TextButton(onClick = onClose) {
            Text(
                "Cancel",
                style =
                    TextStyle(
                        fontFamily = Gilroy,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = colorResource(R.color.primary_blue),
                    ),
            )
          }
        },
    ) {
      DatePicker(state = datePickerState)
    }
  }
}
