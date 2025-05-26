package com.example.pitstopapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pitstopapp.R
import com.example.pitstopapp.data.database.LapTime
import com.example.pitstopapp.data.repositories.LapTimeRepository
import com.example.pitstopapp.ui.PitStopRoute
import com.example.pitstopapp.ui.composables.AppBar
import com.example.pitstopapp.ui.composables.BottomBar
import kotlinx.coroutines.launch

@Composable
fun AddLapTimeScreen(
    navController: NavHostController,
    trackId: Int,
    userId: Int,
    username: String,
    lapTimeRepository: LapTimeRepository
) {
    Scaffold (
        topBar = { AppBar(navController)},
        bottomBar = { BottomBar(navController, username) }
    ) { padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val coroutineScope = rememberCoroutineScope()
            var lapTime by remember { mutableStateOf("") }

            OutlinedTextField(
                value = lapTime,
                onValueChange = { lapTime = it },
                label = { Text("Lap Time") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            val lapTimeTMP = LapTime(
                id = 0,
                trackId = trackId,
                userId = userId,
                lapTime = lapTime
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        lapTimeRepository.insertLapTime(lapTimeTMP)
                    }
                    navController.navigate("${PitStopRoute.Details}/$trackId/$username") {
                        popUpTo("${PitStopRoute.Details}/$trackId/$username") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_time_button))
            }
        }
    }
}