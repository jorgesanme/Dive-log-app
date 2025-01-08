package com.smedina.dive_log_app.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smedina.dive_log_app.ui.features.detail.DetailScreen
import com.smedina.dive_log_app.ui.features.main.MainScreen
import com.smedina.dive_log_app.ui.features.maps.MapScreen
import com.smedina.dive_log_app.ui.features.user.UserImageScreen

@Composable
fun MainNavigationBottomBar() {
    var navigationSelectedItem by remember{ mutableIntStateOf(0) }
    val navController = rememberNavController()

    Scaffold(modifier = Modifier,
        bottomBar = {
            NavigationBar {
                BottomNavigationItem().getBottomNavigationItems().forEachIndexed{ index, navigationItem ->
                    NavigationBarItem(
                        label = { Text(navigationItem.label) },
                        selected = index == navigationSelectedItem,
                        icon = {
                            Icon(
                                imageVector = navigationItem.icon,
                                contentDescription = navigationItem.label
                            )
                        },
                        onClick = {
                            navigationSelectedItem = index
                            navController.navigate(navigationItem.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Screen.Main.route) { MainScreen()  }
            composable(Screen.Detail.route) { DetailScreen()  }
            composable(Screen.Map.route) { MapScreen()  }
            composable(Screen.User.route) { UserImageScreen(modifier = Modifier)  }
        }

    }
}

