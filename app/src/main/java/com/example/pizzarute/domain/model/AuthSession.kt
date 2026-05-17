package com.example.pizzarute.domain.model

data class AuthSession(
    val username: String,
    val email: String,
    val mode: String,
    val isAuthenticated: Boolean,
    val message: String
)
