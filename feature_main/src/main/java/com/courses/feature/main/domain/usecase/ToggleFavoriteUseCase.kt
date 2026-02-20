package com.courses.feature.main.domain.usecase

import com.courses.feature.main.domain.repository.CourseRepository

class ToggleFavoriteUseCase(private val repository: CourseRepository) {
    suspend fun execute(courseId: Int, currentlyLiked: Boolean) =
        repository.toggleFavorite(courseId, currentlyLiked)
}