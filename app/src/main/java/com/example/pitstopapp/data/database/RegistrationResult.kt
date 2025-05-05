package com.example.pitstopapp.data.database

sealed class RegistrationResult {
    data class Success(val message: String) : RegistrationResult()
    data class Failure(val errorMessage: String) : RegistrationResult()
}