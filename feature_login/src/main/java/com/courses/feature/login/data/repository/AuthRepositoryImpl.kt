package com.courses.feature.login.data.repository

import com.courses.feature.login.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        return Result.success(Unit)
    }
}