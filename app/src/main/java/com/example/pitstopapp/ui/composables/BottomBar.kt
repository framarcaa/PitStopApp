package com.example.pitstopapp.ui.composables

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pitstopapp.ui.PitStopRoute

@Composable
fun BottomBar(navController: NavHostController, username: String) {
    BottomAppBar (
        modifier = Modifier.height(70.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        content = {
            IconButton(
                onClick = { navController.navigate("${PitStopRoute.Home}/$username") {
                    popUpTo("${PitStopRoute.Home}/$username") { inclusive = true }
                } },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Home, contentDescription = "Home", tint = MaterialTheme.colorScheme.onSurface)
            }
            IconButton(
                onClick = { navController.navigate("${PitStopRoute.Profile}/$username") {
                    popUpTo("${PitStopRoute.Profile}/$username") { inclusive = true }
                } },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.AccountCircle, contentDescription = "Profile", tint = MaterialTheme.colorScheme.onSurface)
            }
            IconButton(
                onClick = { /*navController.navigate(PitStopRoute.Settings) {
                    popUpTo(PitStopRoute.Settings) { inclusive = true }
                }*/},
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}