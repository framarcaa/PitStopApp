package com.example.pitstopapp.data.repositories

import com.example.pitstopapp.data.database.BestLapResult
import com.example.pitstopapp.data.database.LapTime

interface LapTimeRepositoryInterface {
    suspend fun insertLapTime(lapTime: LapTime)

    suspend fun getLapTimesByTrackId(trackId: Int, callback: UserRepositoryInterface.Callback<List<LapTime>>)

    suspend fun getLapTimeByUserIdAndTrackId(userId: Int, trackId: Int, callback: UserRepositoryInterface.Callback<LapTime>)

    suspend fun getBestLapsTimeByUserId(userId: Int, callback: UserRepositoryInterface.Callback<List<BestLapResult>>)
}