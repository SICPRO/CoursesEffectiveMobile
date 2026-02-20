package com.courses.feature.main.domain.repository

import com.courses.feature.main.data.model.Course

interface CourseRepository {
    suspend fun getCourses(): Result<List<Course>>
    suspend fun toggleFavorite(courseId: Int, currentlyLiked: Boolean)
    suspend fun getFavoriteCourses(): Result<List<Course>>
}