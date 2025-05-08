package com.example.pitstopapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.pitstopapp.data.database.TrackRepository
import com.example.pitstopapp.data.repositories.UserRepository
import com.example.pitstopapp.ui.PitStopNavGraph
import com.example.pitstopapp.ui.composables.AppBar
import com.example.pitstopapp.ui.screens.home.HomeScreen
import com.example.pitstopapp.ui.theme.PitStopAppTheme
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