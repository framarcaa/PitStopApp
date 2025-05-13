package com.example.pitstopapp.data.repositories

import android.app.Application
import com.example.pitstopapp.data.database.LapTime
import com.example.pitstopapp.data.database.LapTimeDAO
import com.example.pitstopapp.data.database.Track
import com.example.pitstopapp.data.database.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LapTimeRepository(application: Application) : LapTimeRepositoryInterface {
    private val lapTimeDAO: LapTimeDAO

    init {
        val db = UserDatabase.getDatabase(application)
        lapTimeDAO = db.lapTimeDAO()
    }

    override suspend fun insertLapTime(lapTime: LapTime) {
        CoroutineScope(Dispatchers.IO).launch {
            lapTimeDAO.insertLapTime(lapTime)
        }
    }

    override suspend fun getLapTimesByTrackId(
        trackId: Int,
        callback: UserRepositoryInterface.Callback<List<LapTime>>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val lapTimes = lapTimeDAO.getLapTimesByTrackId(trackId)
            withContext(Dispatchers.Main) {
                callback.onResult(lapTimes)
            }
        }
    }

    override suspend fun getLapTimeByUserIdAndTrackId(
        userId: Int,
        trackId: Int,
        callback: UserRepositoryInterface.Callback<LapTime>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val lapTime = lapTimeDAO.getLapTimesByUserIdAndTrackId(userId, trackId)
            withContext(Dispatchers.Main) {
                if (lapTime != null) {
                    callback.onResult(lapTime)
                }
            }
        }
    }
}