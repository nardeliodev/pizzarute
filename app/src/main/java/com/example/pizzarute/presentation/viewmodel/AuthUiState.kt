package com.example.pizzarute.presentation.viewmodel

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val userLabel: String = "",
    val email: String = "",
    val mode: String = "DEMO",
    val message: String = ""
)
