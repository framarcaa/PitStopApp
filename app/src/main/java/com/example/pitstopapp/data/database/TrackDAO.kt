package com.example.pitstopapp.data.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TrackDAO {
    @Query("SELECT * FROM Tracks")
    suspend fun getAllTracks(): List<Track>

    @Query("SELECT * FROM Tracks WHERE id = :trackId LIMIT 1")
    suspend fun getTrackById(trackId: String): Track?

    @Query("SELECT * FROM Tracks WHERE name LIKE :query")
    suspend fun searchByName(query: String): List<Track>
}