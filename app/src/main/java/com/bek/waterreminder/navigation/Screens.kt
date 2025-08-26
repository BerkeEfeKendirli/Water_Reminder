package com.bek.waterreminder.navigation

sealed class Screens(val route: String) {
  object Onboarding : Screens("onboarding")

  object Calculation : Screens("calculation")

  object Home : Screens("home")
}
