package com.bek.waterreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.navigation.compose.rememberNavController
import com.bek.waterreminder.data.local.dataStore
import com.bek.waterreminder.navigation.AppNavHost
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val navController = rememberNavController()
      val context = LocalContext.current
      val hasSetWeightFlow = remember {
        context.dataStore.data.map { it[booleanPreferencesKey("has_set_weight")] ?: false }
      }
      val hasSetWeight by hasSetWeightFlow.collectAsState(initial = false)

      Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        AppNavHost(navController = navController, userHasSetWeight = hasSetWeight)
      }
    }
  }
}
