package com.example.pitstopapp.data.repositories

import com.example.pitstopapp.data.database.LapTime
import com.example.pitstopapp.data.database.Track

interface LapTimeRepositoryInterface {
    suspend fun insertLapTime(lapTime: LapTime)

    suspend fun getLapTimesByTrackId(trackId: Int, callback: UserRepositoryInterface.Callback<List<LapTime>>)

    suspend fun getLapTimeByUserIdAndTrackId(userId: Int, trackId: Int, callback: UserRepositoryInterface.Callback<LapTime>)


}