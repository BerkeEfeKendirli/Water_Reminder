package com.bek.waterreminder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bek.waterreminder.ui.screens.LoadingScreen
import com.bek.waterreminder.ui.screens.calculation.CalculationScreen
import com.bek.waterreminder.ui.screens.onboarding.OnboardingScreen
import com.bek.waterreminder.viewmodel.CalculationViewModel
import com.bek.waterreminder.viewmodel.NavigationViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    navigationViewModel: NavigationViewModel,
) {
  val hasSetWeight by navigationViewModel.hasSetWeight.collectAsState()
  when (hasSetWeight) {
    null -> LoadingScreen()
    false -> {
      NavHost(navController = navController, startDestination = Screens.Onboarding.route) {
        composable(Screens.Onboarding.route) {
          OnboardingScreen(onFinished = { navController.navigate(Screens.Calculation.route) })
        }
        composable(Screens.Calculation.route) {
          val calculationViewModel: CalculationViewModel = viewModel()
          CalculationScreen(
              viewModel = calculationViewModel,
              onNavigateToHome = { navController.navigate(Screens.Home.route) },
          )
        }
        composable(Screens.Home.route) { HomeNavigation() }
      }
    }
    true -> {
      NavHost(navController = navController, startDestination = Screens.Home.route) {
        composable(Screens.Home.route) { HomeNavigation() }
      }
    }
  }
}
