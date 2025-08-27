package com.bek.waterreminder.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bek.waterreminder.ui.screens.calculation.CalculationScreen
import com.bek.waterreminder.ui.screens.onboarding.OnboardingScreen
import com.bek.waterreminder.viewmodel.CalculationViewModel

@Composable
fun AppNavHost(navController: NavHostController, userHasSetWeight: Boolean) {
  val calculationViewModel: CalculationViewModel = viewModel()

  NavHost(
      navController = navController,
      startDestination = if (userHasSetWeight) Screens.Home.route else Screens.Onboarding.route,
  ) {
    composable(Screens.Onboarding.route) {
      OnboardingScreen(onFinished = { navController.navigate(Screens.Calculation.route) })
    }
    composable(Screens.Calculation.route) {
      CalculationScreen(
          viewModel = calculationViewModel,
          onNavigateToHome = { navController.navigate(Screens.Home.route) },
      )
    }
    composable(Screens.Home.route) { HomeNavigation() }
  }
}
