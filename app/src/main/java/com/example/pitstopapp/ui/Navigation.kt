package com.example.pitstopapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pitstopapp.ui.screens.home.HomeScreen
import com.example.pitstopapp.ui.screens.profile.ProfileScreen
import kotlinx.serialization.Serializable

sealed interface PitStopRoute {
    @Serializable data object Home : PitStopRoute
    @Serializable data object Profile : PitStopRoute
}

@Composable
fun PitStopNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PitStopRoute.Home,
        modifier = modifier
    ) {
        composable<PitStopRoute.Home> {
            HomeScreen(navController)
        }
        composable<PitStopRoute.Profile> {
            ProfileScreen(navController)
        }
    }
}