package com.example.pitstopapp.ui.screens.login

import android.Manifest
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.pitstopapp.R
import com.example.pitstopapp.data.database.LoginResult
import com.example.pitstopapp.data.repositories.UserRepositoryInterface
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(navController: NavHostController, userRepository: UserRepositoryInterface) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val scope = rememberCoroutineScope()
    val sharedPrefs = remember { context.getSharedPreferences("login_prefs", MODE_PRIVATE) }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var location: Location? by remember { mutableStateOf(null) }

    fun loginUser() {
        if (username.isEmpty() || password.isEmpty()) {
            loginError = "Campi vuoti"
            return
        }

        val trimmedPassword = password.trim()

        // Log prima dell'invio al repository
        Log.d("LoginScreen", "Debug - Tentativo di login con username: $username")
        Log.d("LoginScreen", "Debug - Password inserita (originale): $password")
        Log.d("LoginScreen", "Debug - Password inserita (dopo trim): $trimmedPassword")
        Log.d("LoginScreen", "Debug - Lunghezza password: ${trimmedPassword.length}")

        userRepository.loginUser(username, trimmedPassword, object : UserRepositoryInterface.Callback<LoginResult> {
            override fun onResult(result: LoginResult) {
                scope.launch {
                    withContext(Dispatchers.Main) {
                        when (result) {
                            is LoginResult.Success -> {
                                Log.d("LoginScreen", "Debug - Login riuscito")
                                Log.d("LoginScreen", "Debug - Password hashata nel DB: ${result.user.password}")
                                Log.d("LoginScreen", "Debug - Lunghezza hash nel DB: ${result.user.password.length}")
                                Log.d("LoginScreen", "Debug - Hash inizia con \$2a\$: ${result.user.password.startsWith("\$2a\$")}")

                                Toast.makeText(context, "Login riuscito", Toast.LENGTH_SHORT).show()
                                /*requestNotificationPermission {
                                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED && !hasShownLocationRequest) {
                                        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                        sharedPrefs.edit().putBoolean("location_permission_requested", true).apply()
                                        hasShownLocationRequest = true
                                    } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                                        == PackageManager.PERMISSION_GRANTED) {
                                        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                                            location = if (task.isSuccessful && task.result != null) task.result else null
                                            sendLoginNotification(username, location)
                                        }
                                    } else {
                                        // Invia la notifica anche senza la posizione
                                        sendLoginNotification(username, null)
                                    }
                                }*/
                                navController.navigate("home/${username}") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                            is LoginResult.InvalidCredentials -> {
                                Log.d("LoginScreen", "Debug - Credenziali non valide")
                                loginError = "Credenziali errate"
                            }
                            is LoginResult.UserNotFound -> {
                                Log.d("LoginScreen", "Debug - Utente non trovato nel database")
                                loginError = "Utente non trovato"
                            }
                            is LoginResult.Failure -> {
                                Log.e("LoginScreen", "Debug - Errore durante il login: ${result.errorMessage}")
                                loginError = result.errorMessage
                            }
                        }
                    }
                }
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(217.dp, 233.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Login to PistStopApp", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (loginError.isNotEmpty()) {
            Text(text = loginError, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { loginUser() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("LOGIN")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Non hai un account? Registrati qui",
            color = Color.Blue,
            modifier = Modifier
                .clickable {
                    navController.navigate("registration")
                }
        )
    }
}