package com.example.pitstopapp.data.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TrackDAO {
    @Query("SELECT * FROM Tracks")
    suspend fun getAllTracks(): List<Track>

    @Query("SELECT * FROM Tracks WHERE id = :trackId")
    suspend fun getTrackById(trackId: Int): Track?

    @Query("SELECT * FROM Tracks WHERE name LIKE :query || '%' ORDER BY name ASC")
    suspend fun searchByName(query: String): List<Track>
}