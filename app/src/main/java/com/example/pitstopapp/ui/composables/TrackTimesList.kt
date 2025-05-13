package com.example.pitstopapp.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pitstopapp.data.database.LapTime
import com.example.pitstopapp.data.repositories.LapTimeRepository
import com.example.pitstopapp.data.repositories.UserRepositoryInterface

@Composable
fun TrackTimesList(
    trackId: Int,
    lapTimeRepository: LapTimeRepository
) {
    var times by remember { mutableStateOf<List<LapTime>>(emptyList()) }

    LaunchedEffect(trackId) {
        lapTimeRepository.getLapTimesByTrackId(trackId, object : UserRepositoryInterface.Callback<List<LapTime>> {
            override fun onResult(result: List<LapTime>) {
                times = result.sortedBy { it.lapTime } // Ordina i tempi in ordine crescente
            }
        })
    }

    LazyColumn {
        itemsIndexed(times) { index, lapTime ->
            Row(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = "${index + 1}.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(0.1f)
                )
                Text(
                    text = "User ID: ${lapTime.userId}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(0.4f)
                )
                Text(
                    text = "Tempo: ${lapTime.lapTime}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(0.5f)
                )
            }
        }
    }
}