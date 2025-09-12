package com.bek.waterreminder.navigation

import com.bek.waterreminder.R

sealed class Screens(val route: String) {

  object Onboarding : Screens("onboarding")

  object Calculation : Screens("calculation")

  object Home : Screens("home")
}

sealed class BottomNavScreen(val route: String, val icon: Int, val label: String) {
  object ManageWater :
      BottomNavScreen(
          route = "manage",
          icon = R.drawable.water,
          label = "Drink Water",
      )

  object Activity :
      BottomNavScreen(
          route = "activity",
          icon = R.drawable.activity,
          label = "Check Activity",
      )

  object Settings :
      BottomNavScreen(
          route = "settings",
          icon = R.drawable.settings,
          label = "Settings",
      )
}

val bottomNavItems =
    listOf(
        BottomNavScreen.ManageWater,
        BottomNavScreen.Activity,
        BottomNavScreen.Settings,
    )
