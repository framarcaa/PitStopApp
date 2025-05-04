package com.example.pitstopapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): User?

    @Query("UPDATE users SET profilePictureUri = :pictureUri WHERE username = :username")
    suspend fun updateProfilePicture(username: String, pictureUri: String)

    @Query("UPDATE users SET location = :location WHERE username = :username")
    suspend fun updateLocation(username: String, location: String)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}