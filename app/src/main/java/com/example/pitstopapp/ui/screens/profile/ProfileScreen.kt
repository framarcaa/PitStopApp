package com.example.pitstopapp.ui.screens.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.pitstopapp.data.database.User
import com.example.pitstopapp.data.repositories.UserRepository
import com.example.pitstopapp.data.repositories.UserRepositoryInterface
import com.example.pitstopapp.ui.composables.BottomBar
import com.example.pitstopapp.utils.saveBitmapAsUri
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    userRepository: UserRepository,
    username: String,
    context: Context
) {
    var user by remember { mutableStateOf<User?>(null) }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }
    var location by remember { mutableStateOf("Non impostata") }
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

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
                        profilePictureUri = Uri.parse(uriString)
                    } catch (e: Exception) {
                        // Gestione errore parsing URI
                    }
                }
                location = result?.location ?: "Non impostata"
            }
        })
    }

    Scaffold(
        topBar = { AppBar(navController) },
            /*TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Profilo Utente",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("leaderboard_screen/$username") }) {
                        Icon(
                            Icons.Filled.Leaderboard,
                            contentDescription = "Classifica",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }*/
        bottomBar = { BottomBar(navController = navController, username = username) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .clickable { showDialog = true }
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
                            text = "Posizione: $location",
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
            }
        }
    )

    if (showDialog) {
        showImageSelectionDialog(
            pickImageLauncher = pickImageLauncher,
            takePictureLauncher = takePictureLauncher,
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun showImageSelectionDialog(
    pickImageLauncher: ActivityResultLauncher<String>,
    takePictureLauncher: ActivityResultLauncher<Void?>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text("Seleziona immagine profilo")
        },
        text = {
            Column {
                Text(
                    text = "Scegli un'opzione per aggiornare l'immagine del profilo:",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                takePictureLauncher.launch(null)
                onDismiss()
            }) {
                Text("Scatta una foto")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                pickImageLauncher.launch("image/*")
                onDismiss()
            }) {
                Text("Scegli dalla galleria")
            }
        }
    )
}

fun copyImageToAppStorage(context: Context, uri: Uri): Uri {
    val inputStream = context.contentResolver.openInputStream(uri)
    val fileName = "profile_${System.currentTimeMillis()}.jpg"
    val outputFile = File(context.filesDir, fileName)

    inputStream?.use { input ->
        outputFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }

    return Uri.fromFile(outputFile)
}