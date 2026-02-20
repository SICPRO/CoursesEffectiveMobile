package com.courses.feature.login.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courses.feature.login.domain.usecase.LoginUseCase
import com.courses.feature.login.domain.usecase.ValidateEmailUseCase
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    val loginState: LiveData<LoginState> = _loginState

    fun onLoginClicked(email: String, password: String) {
        when {
            email.isBlank() || password.isBlank() -> {
                _loginState.value = LoginState.Error("Заполните все поля")
            }
            !validateEmailUseCase.execute(email) -> {
                _loginState.value = LoginState.Error("Введите корректный email (example@mail.ru)")
            }
            else -> {
                viewModelScope.launch {
                    _loginState.value = LoginState.Loading
                    val result = loginUseCase.execute(email, password)
                    _loginState.value = if (result.isSuccess) {
                        LoginState.Success
                    } else {
                        LoginState.Error(result.exceptionOrNull()?.message ?: "Ошибка входа")
                    }
                }
            }
        }
    }
}