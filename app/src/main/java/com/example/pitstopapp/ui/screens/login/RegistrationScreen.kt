package com.example.pitstopapp.ui.screens.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pitstopapp.R
import com.example.pitstopapp.data.database.User
import com.example.pitstopapp.data.repositories.UserRepository
import com.example.pitstopapp.data.repositories.UserRepositoryInterface
import com.example.pitstopapp.ui.PitStopRoute

@Composable
fun RegistrationScreen(navController: NavHostController, userRepository: UserRepository) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val sharedPrefs = remember { context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    fun registerUser() {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(context, "Tutti i campi devono essere riempiti", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Le password non coincidono", Toast.LENGTH_SHORT).show()
            return
        }

        userRepository.isUsernameTaken(username, object : UserRepositoryInterface.Callback<Boolean> {
            override fun onResult(isTaken: Boolean) {
                if (isTaken) {
                    Toast.makeText(context, "Username già in uso", Toast.LENGTH_SHORT).show()
                } else {
                    userRepository.isEmailTaken(email, object : UserRepositoryInterface.Callback<Boolean> {
                        override fun onResult(isTaken: Boolean) {
                            if (isTaken) {
                                Toast.makeText(context, "Email già in uso", Toast.LENGTH_SHORT).show()
                            } else {
                                // Passa la password non criptata al repository
                                val newUser = User(username = username, email = email, password = password)
                                userRepository.registerUser(newUser)
                                Toast.makeText(context, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show()

                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    if (!hasNotificationPermission && !hasShownNotificationRequest) {
                                        if (ActivityCompat.shouldShowRequestPermissionRationale(context as ComponentActivity, Manifest.permission.POST_NOTIFICATIONS)) {
                                            Toast.makeText(context, "Permesso notifiche necessario", Toast.LENGTH_SHORT).show()
                                        }
                                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        sharedPrefs.edit().putBoolean("notification_permission_requested", true).apply()
                                        hasShownNotificationRequest = true
                                    } else {
                                        sendWelcomeNotification(username)
                                    }
                                } else {
                                    sendWelcomeNotification(username)
                                }*/
                                navController.navigate("${PitStopRoute.Home}/${newUser.username}")
                            }
                        }
                    })
                }
            }
        })
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.primary
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp)
                .background(MaterialTheme.colorScheme.background)
                .clickable(
                    // Indica che questo clic non deve essere consumato
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus() // Rimuove il focus → chiude la tastiera
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(217.dp, 233.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(R.string.register_title), style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.confirm_password_field)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { registerUser() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text((stringResource(R.string.register_button)))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.not_registered_description),
                color = Color.Blue,
                modifier = Modifier.padding(horizontal = 16.dp)
                    .clickable {
                        navController.navigate(PitStopRoute.Login)
                    }
            )
        }
    }
}