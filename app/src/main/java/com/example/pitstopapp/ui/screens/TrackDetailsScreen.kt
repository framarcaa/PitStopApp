package com.example.pitstopapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.pitstopapp.R
import com.example.pitstopapp.data.database.LapTime
import com.example.pitstopapp.data.database.Track
import com.example.pitstopapp.data.repositories.LapTimeRepository
import com.example.pitstopapp.data.repositories.TrackRepository
import com.example.pitstopapp.data.repositories.UserRepositoryInterface
import com.example.pitstopapp.ui.composables.AppBar
import com.example.pitstopapp.ui.composables.BottomBar
import com.example.pitstopapp.ui.composables.HeaderRow
import com.example.pitstopapp.ui.composables.LapTimeRow
import com.example.pitstopapp.ui.composables.TrackTimesList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun TrackDetailsScreen(
    navController: NavHostController,
    trackId: String,
    username: String,
    trackRepository: TrackRepository,
    lapTimeRepository: LapTimeRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var track by remember { mutableStateOf<Track?>(null) }
    var times by remember { mutableStateOf<List<LapTime>>(emptyList()) }
    fun getTrackById(trackId: Int) : Track? {
        trackRepository.getTrackById(trackId, object : UserRepositoryInterface.Callback<Track?> {
            override fun onResult(result: Track?) {
                track = result
            }
        })
        return track
    }
    suspend fun getLapTimesByTrackId(trackId: Int): List<LapTime> {
        lapTimeRepository.getLapTimesByTrackId(trackId, object : UserRepositoryInterface.Callback<List<LapTime>> {
            override fun onResult(result: List<LapTime>) {
                times = result
            }
        })
        return times
    }

    LaunchedEffect(trackId) {
        track = getTrackById(trackId.toInt())
        times = getLapTimesByTrackId(trackId.toInt())
    }

    Scaffold (
        topBar = { AppBar(navController)},
        bottomBar = { BottomBar(navController, username) }
    ) { padding ->
        LazyColumn (
            modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ){
            item {
                Text(
                    text = track?.name ?: "Circuito non trovato",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                val context = LocalContext.current
                val imageName = track?.imageUri
                val imageResId = if (imageName != null) {
                    context.resources.getIdentifier(imageName, "drawable", context.packageName)
                } else {
                    R.drawable.logo // Sostituisci con un'immagine predefinita
                }
                val imagePainter = rememberAsyncImagePainter(model = imageResId)

                Image(
                    painter = imagePainter,
                    contentDescription = "Track Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // o la metà dell'altezza prevista della card
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = track?.description ?: "Descrizione non disponibile",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = "Lunghezza: ${track?.length ?: 0} m",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))


                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    HeaderRow()
                    times.forEachIndexed { index, lapTime ->
                        LapTimeRow(index + 1, lapTime)
                    }
                }

                Text(
                    text = "Mappa del percorso",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                // Mappa usando Google Maps Compose
                /*Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    GoogleMap(
                        modifier = Modifier.matchParentSize(),
                        cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(location, 14f)
                        }
                    ) {
                        Marker(position = location, title = title)
                    }
                }*/

                Spacer(modifier = Modifier.height(16.dp))

                val lapTimeTMP = LapTime(
                    userId = 2,
                    trackId = 1,
                    lapTime = "1.56"
                )
                Button(
                    onClick = {
                        coroutineScope.launch {
                            lapTimeRepository.insertLapTime(lapTimeTMP)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Aggiungi Tempo")
                }
            }


        }
    }
}

