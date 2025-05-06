package com.example.pitstopapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pitstopapp.data.database.TrackRepository
import com.example.pitstopapp.data.repositories.UserRepository
import com.example.pitstopapp.ui.screens.home.HomeScreen
import com.example.pitstopapp.ui.screens.login.LoginScreen
import com.example.pitstopapp.ui.screens.login.RegistrationScreen
import com.example.pitstopapp.ui.screens.profile.ProfileScreen
import kotlinx.serialization.Serializable

sealed interface PitStopRoute {
    @Serializable data object Login : PitStopRoute
    @Serializable data object Register : PitStopRoute
    @Serializable data object Home : PitStopRoute
    @Serializable data object Profile : PitStopRoute
    @Serializable data object Settings : PitStopRoute
}

@Composable
fun PitStopNavGraph(
    navController: NavHostController,
    userRepository: UserRepository,
    trackRepository: TrackRepository,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PitStopRoute.Login,
        modifier = modifier
    ) {
        composable<PitStopRoute.Login> {
            LoginScreen(navController, userRepository)
        }

        composable<PitStopRoute.Register> {
            RegistrationScreen(navController, userRepository)
        }

        /*composable<PitStopRoute.Home> {
            HomeScreen(navController, userRepository)
        }*/
        composable("${PitStopRoute.Profile}/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "unknown"
            ProfileScreen(navController, userRepository, username, LocalContext.current)
        }

        composable<PitStopRoute.Settings> {
            // SettingsScreen(navController)
        }

        composable("${PitStopRoute.Home}/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "unknown"
            HomeScreen(
                navController = navController,
                userRepository = userRepository,
                username = username,
                trackRepository = trackRepository
                /*filter = "",
                onTrackClick = { track ->
                    navController.navigate("details/${track.id}/$username")*/

            )
        }
    }
}