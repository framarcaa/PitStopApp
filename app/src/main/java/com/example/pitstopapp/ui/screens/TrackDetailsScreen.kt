package com.example.pitstopapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.pitstopapp.R
import com.example.pitstopapp.data.database.LapTime
import com.example.pitstopapp.data.database.Track
import com.example.pitstopapp.data.database.User
import com.example.pitstopapp.data.repositories.LapTimeRepository
import com.example.pitstopapp.data.repositories.TrackRepository
import com.example.pitstopapp.data.repositories.UserRepository
import com.example.pitstopapp.data.repositories.UserRepositoryInterface
import com.example.pitstopapp.ui.PitStopRoute
import com.example.pitstopapp.ui.composables.AppBar
import com.example.pitstopapp.ui.composables.BottomBar
import com.example.pitstopapp.ui.composables.HeaderRow
import com.example.pitstopapp.ui.composables.LapTimeRow
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TrackDetailsScreen(
    navController: NavHostController,
    trackId: String,
    username: String,
    trackRepository: TrackRepository,
    lapTimeRepository: LapTimeRepository,
    userRepository: UserRepository
) {
    var track by remember { mutableStateOf<Track?>(null) }
    var times by remember { mutableStateOf<List<LapTime>>(emptyList()) }
    var user by remember { mutableStateOf<User?>(null) }

    fun getTrackById(trackId: Int) : Track? {
        trackRepository.getTrackById(trackId, object : UserRepositoryInterface.Callback<Track?> {
            override fun onResult(result: Track?) {
                track = result
            }
        })
        return track
    }

    fun getUserByUsername(username: String) : User? {
        userRepository.getUserByUsername(username, object : UserRepositoryInterface.Callback<User?> {
            override fun onResult(result: User?) {
                user = result
            }
        })
        return user
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
        user = getUserByUsername(username)
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
                        text = track?.name ?: stringResource(R.string.circuit_not_found),
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
                        text = track?.description ?: stringResource(R.string.description_not_found),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )

                    Text(
                        text = stringResource(R.string.length) + ": ${track?.length ?: 0} m",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    val locationParts = track?.location?.split(",")
                    val trackLatLng = LatLng(
                        locationParts?.getOrNull(0)?.toDoubleOrNull() ?: 0.0,
                        locationParts?.getOrNull(1)?.toDoubleOrNull() ?: 0.0
                    )
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(trackLatLng, 15f)
                    }
                    LaunchedEffect(trackLatLng) {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(trackLatLng, 15f),
                            1000 // millisecondi di animazione
                        )
                    }
                    Log.d("TrackDetailsScreen", "Track LatLng: $cameraPositionState")

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.circuit_map_description),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            properties = MapProperties(
                                mapType = MapType.SATELLITE,
                                isMyLocationEnabled = false
                            )
                        ) {
                            Marker(
                                state = MarkerState(position = trackLatLng),
                                title = track?.name ?: "Circuito",
                                snippet = track?.description ?: ""
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        HeaderRow()
                        times.forEachIndexed { index, lapTime ->
                            LapTimeRow(index + 1, lapTime, userRepository)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val lapTimeTMP = LapTime(
                        userId = user?.id ?: 0,
                        trackId = trackId.toInt(),
                        lapTime = "1.56"
                    )
                    Button(
                        onClick = {
                            navController.navigate(
                                "${PitStopRoute.AddLapTime}/${trackId}/${user?.id ?: 0}/${username}"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = stringResource(R.string.add_time_button))
                    }
                //}
                Spacer(modifier = Modifier.height(320.dp))
            }


        }
    }
}

