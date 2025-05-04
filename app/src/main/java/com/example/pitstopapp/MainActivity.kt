package com.example.pitstopapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.pitstopapp.data.repositories.UserRepository
import com.example.pitstopapp.ui.PitStopNavGraph
import com.example.pitstopapp.ui.composables.AppBar
import com.example.pitstopapp.ui.screens.home.HomeScreen
import com.example.pitstopapp.ui.theme.PitStopAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PitStopAppTheme {
                val navController = rememberNavController()
                val userRepository = UserRepository(application)
                PitStopNavGraph(navController, userRepository)
            }
        }
    }
}