package com.bek.waterreminder.ui.screens.home.managewater.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bek.waterreminder.R
import com.bek.waterreminder.data.local.dataStore
import com.bek.waterreminder.ui.theme.Gilroy
import com.bek.waterreminder.viewmodel.ManageWaterViewModel
import com.bek.waterreminder.viewmodel.ManageWaterViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun WaterQuantityCard() {
  val context = LocalContext.current
  val dataStore = context.dataStore
  val viewModel: ManageWaterViewModel =
      viewModel(
          factory = ManageWaterViewModelFactory(dataStore),
      )

  val selectedCup by viewModel.selectedCupFlow.collectAsState(200)
  val coroutineScope = rememberCoroutineScope()

  Box(
      modifier =
          Modifier.clip(shape = RoundedCornerShape(8.dp))
              .border(1.dp, color = Color(0xffdee8f5), shape = RoundedCornerShape(8.dp))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
          "Select your preferred cup",
          style =
              TextStyle(
                  fontFamily = Gilroy,
                  fontWeight = FontWeight.Medium,
                  fontSize = 14.sp,
                  color = colorResource(R.color.primary_black),
              ),
      )
      Spacer(modifier = Modifier.height(8.dp))
      val quantities = listOf(200, 330, 500, 1000)
      LazyVerticalGrid(
          columns = GridCells.Fixed(2),
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        items(quantities) { qty ->
          WaterQuantityItem(
              isSelected = qty == selectedCup,
              quantity = qty,
              onClick = { coroutineScope.launch { viewModel.updateSelectedCup(qty) } },
          )
        }
      }
    }
  }
}
