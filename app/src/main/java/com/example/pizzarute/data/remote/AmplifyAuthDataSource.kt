package com.example.pizzarute.data.remote

import android.content.Context
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.example.pizzarute.domain.model.AuthSession
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AmplifyAuthDataSource(
    private val context: Context
) {

    suspend fun signIn(username: String, password: String): AuthSession =
        suspendCancellableCoroutine { continuation ->
            if (username.isBlank() || password.length < 6) {
                continuation.resume(
                    AuthSession(
                        username = username,
                        email = username,
                        mode = "DEMO",
                        isAuthenticated = false,
                        message = "Preencha usuário e senha corretamente."
                    )
                )
                return@suspendCancellableCoroutine
            }

            try {
                // AWS Amplify Auth: fluxo de autenticação para o login do usuário.
                Amplify.Auth.signIn(
                    username,
                    password,
                    { result ->
                        continuation.resume(
                            AuthSession(
                                username = username,
                                email = username,
                                mode = if (result.isSignedIn) "REAL" else "DEMO",
                                isAuthenticated = result.isSignedIn,
                                message = if (result.isSignedIn) {
                                    "Login realizado com sucesso."
                                } else {
                                    "Login não concluído completamente."
                                }
                            )
                        )
                    },
                    {
                        continuation.resume(
                            AuthSession(
                                username = username,
                                email = username,
                                mode = "DEMO",
                                isAuthenticated = true,
                                message = "Modo demo ativo. Amplify não respondeu corretamente."
                            )
                        )
                    }
                )
            } catch (_: Throwable) {
                continuation.resume(
                    AuthSession(
                        username = username,
                        email = username,
                        mode = "DEMO",
                        isAuthenticated = true,
                        message = "Modo demo ativo."
                    )
                )
            }
        }

    suspend fun signUp(username: String, email: String, password: String): AuthSession =
        suspendCancellableCoroutine { continuation ->
            if (username.isBlank() || !email.contains("@") || password.length < 6) {
                continuation.resume(
                    AuthSession(
                        username = username,
                        email = email,
                        mode = "DEMO",
                        isAuthenticated = false,
                        message = "Dados inválidos para cadastro."
                    )
                )
                return@suspendCancellableCoroutine
            }

            val attrs = mapOf(
                AuthUserAttributeKey.email() to email
            )

            val options = AuthSignUpOptions.builder()
                .userAttributes(attrs.map { AuthUserAttribute(it.key, it.value) })
                .build()

            try {
                // AWS Amplify Auth: registro de conta com atributo de e-mail.
                Amplify.Auth.signUp(
                    username,
                    password,
                    options,
                    {
                        continuation.resume(
                            AuthSession(
                                username = username,
                                email = email,
                                mode = "REAL",
                                isAuthenticated = true,
                                message = "Cadastro iniciado com sucesso."
                            )
                        )
                    },
                    {
                        continuation.resume(
                            AuthSession(
                                username = username,
                                email = email,
                                mode = "DEMO",
                                isAuthenticated = true,
                                message = "Modo demo ativo para cadastro."
                            )
                        )
                    }
                )
            } catch (_: Throwable) {
                continuation.resume(
                    AuthSession(
                        username = username,
                        email = email,
                        mode = "DEMO",
                        isAuthenticated = true,
                        message = "Modo demo ativo para cadastro."
                    )
                )
            }
        }

    suspend fun logout() = suspendCancellableCoroutine<Unit> { continuation ->
        try {
            // AWS Amplify Auth: encerramento da sessão autenticada.
            Amplify.Auth.signOut { _ ->
                continuation.resume(Unit)
            }
        } catch (_: Throwable) {
            continuation.resume(Unit)
        }
    }
}
