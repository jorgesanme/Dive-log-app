package com.smedina.dive_log_app.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
){
    fun getBottomNavigationItems(): List<BottomNavigationItem>{
        return listOf(
            BottomNavigationItem(
                label = "list",
                icon =  Icons.Filled.DateRange,
                route = Screen.Main.route
            ),
            BottomNavigationItem(
                label = "Details",
                icon =  Icons.Filled.AddCircle,
                route = Screen.Detail.route
            ),
            BottomNavigationItem(
                label = "Maps",
                icon =  Icons.Filled.LocationOn,
                route = Screen.Map.route
            ),
            BottomNavigationItem(
                label = "User",
                icon =  Icons.Filled.Person,
                route = Screen.User.route
            )
        )
    }
}

sealed class Screen(val route:String){
    object Main: Screen("home")
    object Detail: Screen("detail")
    object Map: Screen("map")
    object User: Screen("user")
}