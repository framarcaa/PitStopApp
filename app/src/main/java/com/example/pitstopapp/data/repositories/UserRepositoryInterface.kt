package com.example.pitstopapp.data.repositories

import com.example.pitstopapp.data.database.LoginResult
import com.example.pitstopapp.data.database.User

interface UserRepositoryInterface {
    fun isUsernameTaken(username: String, callback: Callback<Boolean>)
    fun isEmailTaken(email: String, callback: Callback<Boolean>)
    fun registerUser(user: User)
    fun loginUser(username: String, password: String, callback: Callback<LoginResult>)
    fun getUserByUsername(username: String, callback: Callback<User?>)

    interface Callback<T> {
        fun onResult(result: T)
    }
}