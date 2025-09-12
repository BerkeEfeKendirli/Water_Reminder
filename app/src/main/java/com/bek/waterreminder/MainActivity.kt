package com.bek.waterreminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.bek.waterreminder.navigation.AppNavHost
import com.bek.waterreminder.util.dataStore
import com.bek.waterreminder.util.updateNotificationWorker
import com.bek.waterreminder.viewmodel.NavigationViewModel
import com.bek.waterreminder.viewmodel.NavigationViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

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

    val channel =
        NotificationChannel(
            "water_reminder_channel",
            "Water Reminder",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
    val manager = getSystemService(NotificationManager::class.java)
    manager.createNotificationChannel(channel)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      val permission = Manifest.permission.POST_NOTIFICATIONS
      if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(arrayOf(permission), 0)
      }
    }

    val intervalKey = longPreferencesKey("notification_interval_minutes")
    val prefs = applicationContext.dataStore
    val intervalMinutes = runBlocking { prefs.data.map { it[intervalKey] ?: 60L }.first() }
    updateNotificationWorker(applicationContext, intervalMinutes)

    setContent {
      val context = LocalContext.current
      val navController = rememberNavController()
      val navigationViewModel: NavigationViewModel =
          viewModel(factory = NavigationViewModelFactory(context))
      Scaffold(
          modifier = Modifier.fillMaxSize().safeDrawingPadding(),
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
