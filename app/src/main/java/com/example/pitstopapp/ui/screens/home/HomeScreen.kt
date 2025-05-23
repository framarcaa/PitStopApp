package com.example.pitstopapp.ui.screens.home

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pitstopapp.data.database.Track
import com.example.pitstopapp.ui.composables.AppBar
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pitstopapp.R
import com.example.pitstopapp.data.repositories.TrackRepository
import com.example.pitstopapp.data.database.User
import com.example.pitstopapp.data.repositories.UserRepository
import com.example.pitstopapp.data.repositories.UserRepositoryInterface
import com.example.pitstopapp.ui.composables.BottomBar
import com.example.pitstopapp.ui.composables.TrackCard

@Composable
fun HomeScreen(
    navController: NavHostController,
    userRepository: UserRepository,
    trackRepository: TrackRepository,
    onTrackClick: (Track) -> Unit,
    username: String
) {
    var searchQuery by remember { mutableStateOf("") }
    var trackList by remember { mutableStateOf<List<Track>>(emptyList()) }
    var userId by remember { mutableStateOf("") }
    var showFavorites by remember { mutableStateOf(false) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    fun showTrackList() {
        if (userId.isNotEmpty()) {
            when {
                searchQuery.isNotEmpty() -> {
                    trackRepository.searchByName(searchQuery, object : UserRepositoryInterface.Callback<List<Track>> {
                        override fun onResult(result: List<Track>) {
                            trackList = result
                        }
                    })
                }
                /*showFavorites -> {
                    trackRepository.getFavouriteTracksByUser(userId, object : UserRepositoryInterface.Callback<List<Track>> {
                        override fun onResult(result: List<Track>) {
                            trackList = result.filter { it.category.equals(selectedFilter, ignoreCase = true) || selectedFilter.isEmpty() }
                                .sortedByDescending { it.insertedDate }
                        }
                    })
                }*/
                else -> {
                    trackRepository.getAllTracks(object : UserRepositoryInterface.Callback<List<Track>> {
                        override fun onResult(result: List<Track>) {
                            trackList = result
                        }
                    })
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        userRepository.getUserByUsername(username, object : UserRepositoryInterface.Callback<User?> {
            override fun onResult(result: User?) {
                result?.let {
                    userId = it.username
                    showTrackList()
                }
            }
        })
    }

    LaunchedEffect(searchQuery, showFavorites) {
        if (userId.isNotEmpty()) {
            showTrackList()
        }
    }


    Scaffold (
        topBar = {
            AppBar(navController)
        },
        bottomBar = {
            BottomBar(navController, username)
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text(stringResource(R.string.search_circuit_bar)) },
                shape = RoundedCornerShape(10.dp)
            )

            // Numero di circuiti trovati
            Text(
                text = stringResource(R.string.circuit_found_number) + ": ${trackList.size}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(trackList) { track ->
                    TrackCard(
                        track = track,
                        onDetailsClick = { selectedTrack -> onTrackClick(selectedTrack) },
                        trackRepository = trackRepository,
                        userId = userId
                    )
                }
            }

        }
    }
}