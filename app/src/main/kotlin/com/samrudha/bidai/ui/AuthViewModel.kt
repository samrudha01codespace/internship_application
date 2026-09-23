package com.samrudha.bidai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.samrudha.bidai.data.AuthRepository
import com.samrudha.bidai.data.NetworkModule
import com.samrudha.bidai.data.TokenStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(error = "Please fill in all fields") }
            return
        }
        runAuth { repository.login(email.trim(), password) }
    }

    fun signup(name: String, mobile: String, email: String, password: String) {
        if (name.isBlank() || mobile.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(error = "Please fill in all fields") }
            return
        }
        runAuth { repository.signup(name.trim(), mobile.trim(), email.trim(), password) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun consumeSuccess() {
        _uiState.update { it.copy(success = false) }
    }

    private fun runAuth(
        call: suspend () -> Result<*>
    ) {
        viewModelScope.launch {
            _uiState.update { AuthUiState(isLoading = true) }
            call()
                .onSuccess {
                    _uiState.update { AuthUiState(isLoading = false, success = true) }
                }
                .onFailure { e ->
                    _uiState.update {
                        AuthUiState(
                            isLoading = false,
                            error = e.message ?: "Something went wrong, please try again"
                        )
                    }
                }
        }
    }

    companion object {
        fun factory(tokenStore: TokenStore): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AuthViewModel(
                    AuthRepository(NetworkModule.authApi, tokenStore)
                )
            }
        }
    }
}
