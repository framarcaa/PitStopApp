package com.example.pitstopapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Track::class, User::class, LapTime::class],
    version = 10,
    exportSchema = true
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun trackDAO(): TrackDAO
    abstract fun lapTimeDAO(): LapTimeDAO

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).createFromAsset("user_database.db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}