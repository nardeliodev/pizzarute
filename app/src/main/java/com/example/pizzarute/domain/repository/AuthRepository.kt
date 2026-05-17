package com.example.pizzarute.domain.repository

import com.example.pizzarute.domain.model.AuthSession

interface AuthRepository {
    suspend fun signIn(username: String, password: String): AuthSession
    suspend fun signUp(username: String, email: String, password: String): AuthSession
    suspend fun logout()
}
