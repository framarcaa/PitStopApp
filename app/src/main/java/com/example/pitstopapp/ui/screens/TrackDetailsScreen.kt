package com.example.pitstopapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.pitstopapp.data.database.Track
import com.example.pitstopapp.data.database.TrackRepository
import com.example.pitstopapp.data.repositories.UserRepositoryInterface
import com.example.pitstopapp.ui.composables.AppBar
import com.example.pitstopapp.ui.composables.BottomBar

@Composable
fun TrackDetailsScreen(
    navController: NavHostController,
    trackId: String,
    username: String,
    trackRepository: TrackRepository
) {
    var track by remember { mutableStateOf<Track?>(null) }
    fun getTrackById(trackId: String): Track? {
        trackRepository.getTrackById(trackId.toInt(), object : UserRepositoryInterface.Callback<Track?> {
            override fun onResult(result: Track?) {
                track = result
            }
        })
    }

    LaunchedEffect(trackId) {
        track = trackRepository.getTrackById(trackId.toInt())
    }

    Scaffold (
        topBar = { AppBar(navController)},
        bottomBar = { BottomBar(navController, username) }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)) {

            Text(
                text = track?.name ?: "Circuito non trovato",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            val context = LocalContext.current
            val imageName = track?.imageUri
            val imageResId = remember(imageName) {
                context.resources.getIdentifier(imageName, "drawable", context.packageName)
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
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Lunghezza: %.3f km".format(track?.length ?: 0.0),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Mappa del percorso",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Mappa usando Google Maps Compose
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                /*GoogleMap(
                    modifier = Modifier.matchParentSize(),
                    cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(location, 14f)
                    }
                ) {
                    Marker(position = location, title = title)
                }*/
            }
        }
    }

}