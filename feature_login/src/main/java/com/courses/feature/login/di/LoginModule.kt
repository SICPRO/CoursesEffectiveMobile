package com.courses.feature.login.di

import com.courses.feature.login.data.repository.AuthRepositoryImpl
import com.courses.feature.login.domain.repository.AuthRepository
import com.courses.feature.login.domain.usecase.LoginUseCase
import com.courses.feature.login.domain.usecase.ValidateEmailUseCase
import com.courses.feature.login.presentation.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    factory { ValidateEmailUseCase() }
    factory { LoginUseCase(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
}