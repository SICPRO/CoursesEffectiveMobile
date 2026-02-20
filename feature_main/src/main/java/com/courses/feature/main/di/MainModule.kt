package com.courses.feature.main.di

import androidx.room.Room
import com.courses.feature.main.data.api.CoursesApi
import com.courses.feature.main.data.local.AppDatabase
import com.courses.feature.main.data.repository.CourseRepositoryImpl
import com.courses.feature.main.domain.repository.CourseRepository
import com.courses.feature.main.domain.usecase.GetCoursesUseCase
import com.courses.feature.main.domain.usecase.GetFavoriteCoursesUseCase
import com.courses.feature.main.domain.usecase.ToggleFavoriteUseCase
import com.courses.feature.main.presentation.favorites.FavoritesViewModel
import com.courses.feature.main.presentation.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val mainModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://drive.usercontent.google.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(CoursesApi::class.java) }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "courses_db")
            .build()
    }

    single<CourseRepository> { CourseRepositoryImpl(get(), get()) }

    factory { GetCoursesUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
    factory { GetFavoriteCoursesUseCase(get()) }

    viewModel { MainViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }
}