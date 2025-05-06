package com.example.pitstopapp.data.database

import com.example.pitstopapp.data.repositories.UserRepositoryInterface

interface TrackRepositoryInterface {
    fun getAllTracks(callback: UserRepositoryInterface.Callback<List<Track>>)

    fun getTrackById(trackId: String, callback: UserRepositoryInterface.Callback<Track?>)

    fun searchByName(query: String, callback: UserRepositoryInterface.Callback<List<Track>>)
}