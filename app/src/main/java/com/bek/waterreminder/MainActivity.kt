package com.bek.waterreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.bek.waterreminder.navigation.AppNavHost
import com.bek.waterreminder.viewmodel.NavigationViewModel
import com.bek.waterreminder.viewmodel.NavigationViewModelFactory

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.White.toArgb()
    window.navigationBarColor = Color.White.toArgb()
    WindowInsetsControllerCompat(window, window.decorView).apply {
      isAppearanceLightStatusBars = true
      isAppearanceLightNavigationBars = true
    }
    setContent {
      val context = LocalContext.current
      val navController = rememberNavController()
      val navigationViewModel: NavigationViewModel =
          viewModel(factory = NavigationViewModelFactory(context))
      Scaffold(
          modifier = Modifier.fillMaxSize().safeDrawingPadding(), // Safe area
      ) { innerPadding ->
        AppNavHost(
            navController = navController,
            navigationViewModel = navigationViewModel,
            modifier = Modifier.padding(innerPadding).safeDrawingPadding(),
        )
      }
    }
  }
}
