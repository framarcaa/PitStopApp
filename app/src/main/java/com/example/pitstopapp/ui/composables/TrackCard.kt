package com.example.pitstopapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.pitstopapp.R
import com.example.pitstopapp.data.database.Track
import com.example.pitstopapp.data.repositories.TrackRepository

@Composable
fun TrackCard(
    track: Track,
    onDetailsClick: (Track) -> Unit,
    modifier: Modifier = Modifier,
    trackRepository: TrackRepository,
    userId: String
) {


    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
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
                    Button(onClick = { onDetailsClick(track) }) {
                        Text(stringResource(R.string.details_screen_name))
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {}) {
                            Icon(Icons.Outlined.StarOutline, contentDescription = "Star")
                        }
                        /*Icon(
                            if(isStar) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = if (isStar) "Favourite filled" else "Favourite",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    val newState = !isStar
                                    isStar = newState
                                    trackRepository.updateFavouriteStatus(track.id, userId, newState)
                                }
                        )*/
                    }
                }
            }
        }
    }
}