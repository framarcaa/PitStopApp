package com.example.pitstopapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pitstopapp.R
import com.example.pitstopapp.data.database.LapTime
import com.example.pitstopapp.data.database.User
import com.example.pitstopapp.data.repositories.UserRepository
import com.example.pitstopapp.data.repositories.UserRepositoryInterface

@Composable
fun HeaderRow() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.Red)
            .border(1.dp, Color.Black)
            .padding(vertical = 8.dp)
    ) {
        listOf(stringResource(R.string.position), stringResource(R.string.pilot), stringResource(R.string.best_lap)).forEach {
            Text(
                text = it,
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LapTimeRow(index: Int, laptime: LapTime, userRepository: UserRepository) {
    var username by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(laptime.userId) {
        userRepository.getUsernameById(laptime.userId, object : UserRepositoryInterface.Callback<String?> {
            override fun onResult(result: String?) {
                username = result
            }
        })
    }

    val backgroundColor = when (index) {
        1 -> Color(0xFFFFD700) // oro
        2 -> Color(0xFFC0C0C0) // argento
        3 -> Color(0xFFCD7F32) // bronzo
        else -> Color.Black
    }

    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(	0xff453d3d))
            .border(1.dp, Color.Black)
    ) {
        Text(index.toString(), Modifier.weight(1f).background(backgroundColor), color = Color.White, textAlign = TextAlign.Center)
        Text(username.toString(), Modifier.weight(2f), color = Color.White, textAlign = TextAlign.Center)
        Text(laptime.lapTime, Modifier.weight(1f), color = Color.White, textAlign = TextAlign.Center)
    }
}