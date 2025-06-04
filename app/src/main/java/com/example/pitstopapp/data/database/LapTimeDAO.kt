package com.example.pitstopapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

data class BestLapResult(
    val name: String,
    val lapTime: Float
)

@Dao
interface LapTimeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLapTime(lapTime: LapTime)

    @Query("SELECT * FROM LapTimes WHERE trackId = :trackId ORDER BY lapTime ASC")
    suspend fun getLapTimesByTrackId(trackId: Int): List<LapTime>

    @Query("SELECT * FROM LapTimes WHERE userId = :userId AND trackId = :trackId")
    suspend fun getLapTimesByUserIdAndTrackId(userId: Int, trackId: Int): LapTime?

    @Query("SELECT T.name AS name, L.lapTime AS lapTime FROM LapTimes AS L JOIN Tracks AS T ON L.trackId = T.id WHERE L.userId = :userId")
    suspend fun getBestLapsTimeByUserId(userId: Int): List<BestLapResult>
}