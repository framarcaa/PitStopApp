package com.example.pitstopapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.pitstopapp.R
import com.example.pitstopapp.data.database.Track
import com.example.pitstopapp.data.database.TrackRepository

@Composable
fun TrackCard(
    track: Track,
    onDetailsClick: (Track) -> Unit,
    modifier: Modifier = Modifier,
    trackRepository: TrackRepository,
    userId: String
) {
    /*var isStarred by remember(track.id) {
        mutableStateOf(track.favourite)
    }*/

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        /*Box(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp)) {
                val context = LocalContext.current
                val imageName = track.imageUri

                val imageResId = remember(imageName) {
                    context.resources.getIdentifier(imageName, "drawable", context.packageName)
                }
                val imagePainter = rememberAsyncImagePainter(
                    model = imageResId
                )
                Image(
                    painter = imagePainter,
                    contentDescription = "Track Image",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 16.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = track.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Descrizione: ${track.description}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Lunghezza: ${track.length} m",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { *//*onDetailsClick(track)*//* }) {
                            Text("Vedi dettagli")
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            *//*Icon(
                                painter = painterResource(
                                    id = if (isStarred) R.drawable.ic_favorite_filled else R.drawable.ic_favorite
                                ),
                                contentDescription = if (isStarred) "Favourite filled" else "Favourite",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        val newState = !isStarred
                                        isStarred = newState
                                        trackRepository.updateFavouriteStatus(track.id, userId, newState)
                                    }
                            )*//*
                        }
                    }
                }
            }
        }*/
        Column(modifier = Modifier.fillMaxWidth()) {
            // Parte superiore con immagine a tutta larghezza
            val context = LocalContext.current
            val imageName = track.imageUri
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

            // Parte inferiore con dettagli
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Text(
                    text = track.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Descrizione: ${track.description}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "Lunghezza: ${track.length} m",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { /*onDetailsClick(track)*/ }) {
                        Text("Vedi dettagli")
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Qui puoi reinserire l'icona preferita se vuoi
                    }
                }
            }
        }
    }
}