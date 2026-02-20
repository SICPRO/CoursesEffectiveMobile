package com.courses.feature.main.domain.usecase

import com.courses.feature.main.data.model.Course
import com.courses.feature.main.domain.repository.CourseRepository

class GetFavoriteCoursesUseCase(private val repository: CourseRepository) {
    suspend fun execute(): Result<List<Course>> = repository.getFavoriteCourses()
}