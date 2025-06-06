package com.example.pitstopapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Tracks")
data class Track(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val imageUri: String?,

    @ColumnInfo
    val description: String,

    @ColumnInfo
    val location: String,

    @ColumnInfo
    val length: String,

    @ColumnInfo
    val country: String
)

@Entity(
    tableName = "Users",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val username: String,

    @ColumnInfo
    val email: String,

    @ColumnInfo
    val password: String,

    @ColumnInfo
    val profilePictureUri: String? = null,

    @ColumnInfo
    val location: String? = null
)

@Entity(
    tableName = "LapTimes",
    indices = [Index(value = ["userId", "trackId"], unique = true)]
)
data class LapTime(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val userId: Int,

    @ColumnInfo
    val trackId: Int,

    @ColumnInfo
    val lapTime: String
)