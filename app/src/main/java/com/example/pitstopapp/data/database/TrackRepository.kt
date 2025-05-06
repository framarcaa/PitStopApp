package com.example.pitstopapp.data.database

import android.app.Application
import com.example.pitstopapp.data.repositories.UserRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackRepository(application: Application) : TrackRepositoryInterface {
    private val trackDAO: TrackDAO

    init {
        val db = UserDatabase.getDatabase(application)
        trackDAO = db.trackDAO()
    }

    override fun getAllTracks(callback: UserRepositoryInterface.Callback<List<Track>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val tracks = trackDAO.getAllTracks()
            withContext(Dispatchers.Main) {
                callback.onResult(tracks)
            }
        }
    }

    override fun getTrackById(trackId: String, callback: UserRepositoryInterface.Callback<Track?>) {
        CoroutineScope(Dispatchers.IO).launch {
            val track = trackDAO.getTrackById(trackId)
            withContext(Dispatchers.Main) {
                callback.onResult(track)
            }
        }
    }

    override fun searchByName(
        query: String,
        callback: UserRepositoryInterface.Callback<List<Track>>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val filteredTracks = trackDAO.searchByName(query)
            withContext(Dispatchers.Main) {
                callback.onResult(filteredTracks)
            }
        }
    }
}