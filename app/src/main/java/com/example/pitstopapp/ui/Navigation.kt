package com.example.pitstopapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pitstopapp.data.database.TrackRepository
import com.example.pitstopapp.data.repositories.UserRepository
import com.example.pitstopapp.ui.screens.TrackDetailsScreen
import com.example.pitstopapp.ui.screens.home.HomeScreen
import com.example.pitstopapp.ui.screens.login.LoginScreen
import com.example.pitstopapp.ui.screens.login.RegistrationScreen
import com.example.pitstopapp.ui.screens.profile.ProfileScreen
import com.example.pitstopapp.ui.screens.settings.SettingsScreen
import com.example.pitstopapp.ui.theme.PitStopAppTheme
import kotlinx.serialization.Serializable

sealed interface PitStopRoute {
    @Serializable data object Login : PitStopRoute
    @Serializable data object Register : PitStopRoute
    @Serializable data object Home : PitStopRoute
    @Serializable data object Profile : PitStopRoute
    @Serializable data object Settings : PitStopRoute
    @Serializable data object Details : PitStopRoute
}

@Composable
fun PitStopNavGraph(
    navController: NavHostController,
    userRepository: UserRepository,
    trackRepository: TrackRepository,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onUsernameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    navController.addOnDestinationChangedListener { _, _, arguments ->
        arguments?.getString("username")?.let { username ->
            onUsernameChange(username)
        }
    }

    NavHost(
        navController = navController,
        startDestination = PitStopRoute.Login,
        modifier = modifier
    ) {
        composable<PitStopRoute.Login> {
            PitStopAppTheme(darkTheme = false) {
                LoginScreen(navController, userRepository)
            }
        }

        composable<PitStopRoute.Register> {
            PitStopAppTheme(darkTheme = false) {
                RegistrationScreen(navController, userRepository)
            }
        }

        composable("${PitStopRoute.Profile}/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "unknown"
            PitStopAppTheme(darkTheme = isDarkTheme) {
                ProfileScreen(navController, userRepository, username, LocalContext.current)
            }

        }

        composable("${PitStopRoute.Settings}/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "unknown"
            PitStopAppTheme(darkTheme = isDarkTheme) {
                SettingsScreen(
                    navController = navController,
                    onThemeToggle = onThemeChange,
                    isDarkTheme = isDarkTheme,
                    username = username
                )
            }
        }

        composable("${PitStopRoute.Home}/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "unknown"
            PitStopAppTheme(darkTheme = isDarkTheme) {
                HomeScreen(
                    navController = navController,
                    userRepository = userRepository,
                    username = username,
                    trackRepository = trackRepository,
                    onTrackClick = { track ->
                        navController.navigate("${PitStopRoute.Details}/${track.id}/$username")
                    }
                )
            }
        }

        composable("${PitStopRoute.Details}/{trackId}/{username}") { backStackEntry ->
            val trackId = backStackEntry.arguments?.getString("trackId") ?: "unknown"
            val username = backStackEntry.arguments?.getString("username") ?: "unknown"
            PitStopAppTheme(darkTheme = isDarkTheme) {
                TrackDetailsScreen(navController, trackId, username, trackRepository)
            }
        }
    }
}