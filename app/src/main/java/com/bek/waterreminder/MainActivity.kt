package com.bek.waterreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.bek.waterreminder.navigation.AppNavHost
import com.bek.waterreminder.viewmodel.NavigationViewModel
import com.bek.waterreminder.viewmodel.NavigationViewModelFactory

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val context = LocalContext.current
      val navController = rememberNavController()
      val navigationViewModel: NavigationViewModel =
          viewModel(factory = NavigationViewModelFactory(context))
      Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        AppNavHost(navController = navController, navigationViewModel = navigationViewModel)
      }
    }
  }
}
