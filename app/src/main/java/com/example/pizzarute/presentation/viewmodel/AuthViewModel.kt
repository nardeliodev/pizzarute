package com.example.pizzarute.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzarute.domain.usecase.LogoutUseCase
import com.example.pizzarute.domain.usecase.SignInUseCase
import com.example.pizzarute.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = "Entrando...")
            runCatching { signInUseCase(username, password) }
                .onSuccess { session ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = session.isAuthenticated,
                        userLabel = session.username,
                        email = session.email,
                        mode = session.mode,
                        message = session.message
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = false,
                        message = error.message ?: "Falha ao entrar."
                    )
                }
        }
    }

    fun signUp(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = "Criando conta...")
            runCatching { signUpUseCase(username, email, password) }
                .onSuccess { session ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = session.isAuthenticated,
                        userLabel = session.username,
                        email = session.email,
                        mode = session.mode,
                        message = session.message
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = false,
                        message = error.message ?: "Falha ao cadastrar."
                    )
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            runCatching { logoutUseCase() }
            _uiState.value = AuthUiState(message = "Sessão encerrada.")
        }
    }
}
