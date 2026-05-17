package com.example.pizzarute.domain.usecase

import com.example.pizzarute.domain.model.AuthSession
import com.example.pizzarute.domain.repository.AuthRepository

class SignInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): AuthSession {
        return repository.signIn(username, password)
    }
}
