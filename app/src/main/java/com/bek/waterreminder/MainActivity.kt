package com.bek.waterreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.bek.waterreminder.ui.screens.onboarding.OnboardingScreen
import com.bek.waterreminder.ui.theme.WaterReminderTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      WaterReminderTheme { Surface(modifier = Modifier.fillMaxSize()) { OnboardingScreen() } }
    }
  }
}
