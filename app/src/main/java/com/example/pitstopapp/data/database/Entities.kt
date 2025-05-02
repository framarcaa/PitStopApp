package com.example.pitstopapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
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