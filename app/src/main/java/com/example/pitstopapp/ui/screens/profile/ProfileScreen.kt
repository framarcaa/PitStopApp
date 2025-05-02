package com.example.pitstopapp.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pitstopapp.ui.composables.AppBar
import androidx.compose.ui.text.font.FontWeight


@Composable
fun ProfileScreen(navController: NavHostController) {
    Scaffold (
        topBar = { AppBar(navController) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Card(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                shape = CircleShape,
                colors = MaterialTheme.colorScheme.run {
                    CardDefaults.cardColors(containerColor = surfaceVariant)
                }
            ) {
                // Placeholder for profile image
            }

            // User Name
            Text(
                text = "Nome Utente",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Email
            Text(
                text = "email@example.com",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}