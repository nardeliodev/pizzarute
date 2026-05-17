package com.example.pizzarute.domain.usecase

import com.example.pizzarute.domain.model.AuthSession
import com.example.pizzarute.domain.repository.AuthRepository

class SignUpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String): AuthSession {
        return repository.signUp(username, email, password)
    }
}
