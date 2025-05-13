package com.example.pitstopapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.pitstopapp.data.repositories.LapTimeRepository
import com.example.pitstopapp.data.repositories.TrackRepository
import com.example.pitstopapp.data.repositories.UserRepository
import com.example.pitstopapp.ui.PitStopNavGraph
import com.example.pitstopapp.utils.ThemeManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var themeManager: ThemeManager
    private var currentUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userRepository = UserRepository(application)
        val trackRepository = TrackRepository(application)
        val lapTimeRepository = LapTimeRepository(application)
        themeManager = ThemeManager(applicationContext)

        setContent {

            val navController = rememberNavController()
            var username by remember { mutableStateOf<String?>(null) }
            val themeState = themeManager.isDarkThemeForUser(username ?: "").collectAsState(initial = false)
            val isDarkTheme = themeState.value

            DisposableEffect(Unit) {
                onDispose {
                    currentUsername = username
                }
            }
            PitStopNavGraph(
                navController,
                userRepository,
                trackRepository,
                lapTimeRepository,
                isDarkTheme,
                onThemeChange = { newTheme ->
                    Log.d("MainActivity", "Setting theme for user: $username to $newTheme")
                    username?.let { currentUser ->
                        lifecycleScope.launch {
                            themeManager.setDarkTheme(currentUser, newTheme)
                        }
                    }
                },
                onUsernameChange = { newUsername ->
                    username = newUsername
                }
            )
        }
    }
}