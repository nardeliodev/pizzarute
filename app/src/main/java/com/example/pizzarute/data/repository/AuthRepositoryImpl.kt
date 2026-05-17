package com.example.pizzarute.data.repository

import com.example.pizzarute.data.remote.AmplifyAuthDataSource
import com.example.pizzarute.domain.model.AuthSession
import com.example.pizzarute.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val dataSource: AmplifyAuthDataSource
) : AuthRepository {

    override suspend fun signIn(username: String, password: String): AuthSession {
        return dataSource.signIn(username, password)
    }

    override suspend fun signUp(username: String, email: String, password: String): AuthSession {
        return dataSource.signUp(username, email, password)
    }

    override suspend fun logout() {
        dataSource.logout()
    }
}
