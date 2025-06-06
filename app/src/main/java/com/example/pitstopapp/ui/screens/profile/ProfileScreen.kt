package com.example.pitstopapp.ui.screens.profile

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pitstopapp.ui.composables.AppBar
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.pitstopapp.R
import com.example.pitstopapp.data.database.BestLapResult
import com.example.pitstopapp.data.database.User
import com.example.pitstopapp.data.repositories.LapTimeRepository
import com.example.pitstopapp.data.repositories.UserRepository
import com.example.pitstopapp.data.repositories.UserRepositoryInterface
import com.example.pitstopapp.ui.composables.BestLapBarChart
import com.example.pitstopapp.ui.composables.BottomBar
import com.example.pitstopapp.utils.saveBitmapAsUri
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.Locale
import androidx.core.net.toUri

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userRepository: UserRepository,
    lapTimeRepository: LapTimeRepository,
    username: String,
    context: Context
) {
    var user by remember { mutableStateOf<User?>(null) }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }
    var location by remember { mutableStateOf("Non impostata") }
    var showDialog by remember { mutableStateOf(false) }
    var bestLaps by remember { mutableStateOf<List<BestLapResult>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val granted = permissions.values.all { it }
            if (!granted) {
                Toast.makeText(context, "Permessi non concessi", Toast.LENGTH_SHORT).show()
            }
        }
    )

    fun requestImagePermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val permanentUri = copyImageToAppStorage(context, it)
                profilePictureUri = permanentUri
                coroutineScope.launch {
                    userRepository.updateProfilePicture(username, permanentUri.toString())
                }
            }
        }
    )

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let {
                val savedUri = saveBitmapAsUri(context, it)
                savedUri?.let { uri ->
                    val permanentUri = copyImageToAppStorage(context, uri)
                    profilePictureUri = permanentUri
                    coroutineScope.launch {
                        userRepository.updateProfilePicture(username, permanentUri.toString())
                    }
                }
            }
        }
    )

    LaunchedEffect(username) {
        userRepository.getUserByUsername(username, object : UserRepositoryInterface.Callback<User?> {
            override fun onResult(result: User?) {
                user = result
                result?.profilePictureUri?.let { uriString ->
                    try {
                        profilePictureUri = uriString.toUri()
                    } catch (e: Exception) {
                        // Gestione errore parsing URI
                    }
                }
                location = result?.location ?: "Non impostata"
            }
        })
    }

    LaunchedEffect(user?.id) {
        lapTimeRepository.getBestLapsTimeByUserId(user?.id ?: 0, object : UserRepositoryInterface.Callback<List<BestLapResult>> {
            override fun onResult(result: List<BestLapResult>) {
                bestLaps = result
            }
        })
    }

    Scaffold(
        topBar = { AppBar(navController) },
        bottomBar = { BottomBar(navController = navController, username = username) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = if (profilePictureUri != null) {
                            rememberAsyncImagePainter(profilePictureUri)
                        } else {
                            painterResource(id = R.drawable.ic_default_profile)
                        },
                        contentDescription = "Immagine del profilo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .clickable {
                                requestImagePermissions()
                                showDialog = true
                            }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = user?.username ?: "Username non disponibile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user?.email ?: "Email non disponibile",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.position) + ": $location",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                                    loc?.let {
                                        val geocoder = Geocoder(context, Locale.getDefault())
                                        val address = geocoder.getFromLocation(
                                            loc.latitude,
                                            loc.longitude,
                                            1
                                        )?.firstOrNull()?.locality ?: "Città sconosciuta"
                                        location = address
                                        coroutineScope.launch {
                                            userRepository.updateLocation(username, address)
                                        }
                                    } ?: run {
                                        Toast.makeText(
                                            context,
                                            "Impossibile ottenere la posizione",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                ActivityCompat.requestPermissions(
                                    context as android.app.Activity,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    1
                                )
                            }
                        },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.update_position_button))
                    }

                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Log.d("ProfileScreen", "Best Laps: $bestLaps")
            BestLapBarChart(bestLaps.map { it.name to it.lapTime })
            Spacer(modifier = Modifier.height(120.dp))
        }
    }

    if (showDialog) {
        ShowImageSelectionDialog(
            pickImageLauncher = pickImageLauncher,
            takePictureLauncher = takePictureLauncher,
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun ShowImageSelectionDialog(
    pickImageLauncher: ActivityResultLauncher<String>,
    takePictureLauncher: ActivityResultLauncher<Void?>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(stringResource(R.string.select_image_title))
        },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.select_image_description),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                takePictureLauncher.launch(null)
                onDismiss()
            }) {
                Text(stringResource(R.string.take_picture_button))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                pickImageLauncher.launch("image/*")
                onDismiss()
            }) {
                Text(stringResource(R.string.gallery_button))
            }
        }
    )
}

fun copyImageToAppStorage(context: Context, uri: Uri): Uri {
    val resolver = context.contentResolver
    val fileName = "pitstop_profile_${System.currentTimeMillis()}.jpg"

    val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val newImageDetails = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PitStop")
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val imageUri = resolver.insert(imageCollection, newImageDetails)!!

    resolver.openOutputStream(imageUri)?.use { outputStream ->
        resolver.openInputStream(uri)?.use { inputStream ->
            inputStream.copyTo(outputStream)
        }
    }

    newImageDetails.clear()
    newImageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
    resolver.update(imageUri, newImageDetails, null, null)

    return imageUri
}

