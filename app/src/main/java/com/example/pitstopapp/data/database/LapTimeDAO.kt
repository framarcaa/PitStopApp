package com.example.pitstopapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LapTimeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLapTime(lapTime: LapTime)

    @Query("SELECT * FROM LapTimes WHERE trackId = :trackId ORDER BY lapTime ASC")
    suspend fun getLapTimesByTrackId(trackId: Int): List<LapTime>

    @Query("SELECT * FROM LapTimes WHERE userId = :userId AND trackId = :trackId")
    suspend fun getLapTimesByUserIdAndTrackId(userId: Int, trackId: Int): LapTime?
}