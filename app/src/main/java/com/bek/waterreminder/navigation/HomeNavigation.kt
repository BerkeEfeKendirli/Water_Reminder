package com.bek.waterreminder.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bek.waterreminder.R
import com.bek.waterreminder.ui.screens.home.activity.ActivityScreen
import com.bek.waterreminder.ui.screens.home.managewater.ManageWaterScreen
import com.bek.waterreminder.ui.screens.home.settings.SettingsScreen
import com.bek.waterreminder.ui.theme.Gilroy

@Composable
fun HomeNavigation() {
  val bottomNavController = rememberNavController()

  Scaffold(
      bottomBar = {
        val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
        NavigationBar(
            containerColor = Color.White,
            contentColor = Color(0xff616161),
            modifier =
                Modifier.height(56.dp).drawBehind {
                  val strokeWidth = 2.dp.toPx()
                  drawLine(
                      color = Color(0xffdee8f5),
                      start = Offset(0f, 0f),
                      end = Offset(size.width, 0f),
                      strokeWidth = strokeWidth,
                  )
                },
        ) {
          bottomNavItems.forEach { screen ->
            val selected = navBackStackEntry?.destination?.route == screen.route
            NavigationBarItem(
                icon = {
                  Image(
                      painter = painterResource(screen.icon),
                      contentDescription = screen.label,
                      colorFilter =
                          ColorFilter.tint(if (selected) Color(0xff327ac9) else Color(0xff616161)),
                  )
                },
                label = {
                  Text(
                      screen.label,
                      style =
                          TextStyle(
                              fontFamily = Gilroy,
                              fontWeight = FontWeight.Medium,
                              fontSize = 10.sp,
                          ),
                  )
                },
                selected = navBackStackEntry?.destination?.route == screen.route,
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xff327ac9),
                        selectedTextColor = Color(0xff327ac9),
                        unselectedIconColor = Color(0xff616161),
                        unselectedTextColor = Color(0xff616161),
                        indicatorColor = Color.Transparent,
                    ),
                onClick = {
                  bottomNavController.navigate(screen.route) {
                    popUpTo(bottomNavController.graph.startDestinationId)
                    launchSingleTop = true
                  }
                },
            )
          }
        }
      },
      topBar = {
        NavigationBar(modifier = Modifier.height(56.dp), containerColor = Color.White) {
          Row(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
                "HydrateMe",
                style =
                    TextStyle(
                        fontFamily = Gilroy,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = colorResource(R.color.primary_black),
                    ),
            )
            Image(
                painter = painterResource(R.drawable.bell),
                contentDescription = "Notifications",
            )
          }
        }
      },
  ) { innerPadding ->
    NavHost(
        navController = bottomNavController,
        startDestination = BottomNavScreen.ManageWater.route,
        modifier = Modifier.padding(innerPadding),
    ) {
      composable(BottomNavScreen.ManageWater.route) { ManageWaterScreen() }
      composable(BottomNavScreen.Activity.route) { ActivityScreen() }
      composable(BottomNavScreen.Settings.route) { SettingsScreen() }
    }
  }
}
