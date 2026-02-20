package com.courses.feature.login.domain.usecase

import com.courses.feature.login.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase
) {
    suspend fun execute(email: String, password: String): Result<Unit> {
        if (!validateEmailUseCase.execute(email)) {
            return Result.failure(Exception("Некорректный email"))
        }
        if (password.isBlank()) {
            return Result.failure(Exception("Пароль не может быть пустым"))
        }
        return authRepository.login(email, password)
    }
}