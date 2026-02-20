package com.courses.feature.main.data.api

import com.courses.feature.main.data.model.CoursesResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface CoursesApi {
    @GET
    suspend fun getCourses(@Url url: String): CoursesResponse
}