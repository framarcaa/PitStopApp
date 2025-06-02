package com.example.pitstopapp.data.database

sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    data class Failure(val errorMessage: String) : LoginResult()
    data object InvalidCredentials : LoginResult()
    data object UserNotFound : LoginResult()
}