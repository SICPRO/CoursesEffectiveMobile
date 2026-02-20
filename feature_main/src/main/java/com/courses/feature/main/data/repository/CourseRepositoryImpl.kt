package com.courses.feature.main.data.repository

import com.courses.feature.main.data.api.CoursesApi
import com.courses.feature.main.data.local.AppDatabase
import com.courses.feature.main.data.local.FavoriteCourse
import com.courses.feature.main.data.model.Course
import com.courses.feature.main.data.model.CourseDto
import com.courses.feature.main.domain.repository.CourseRepository
import java.text.SimpleDateFormat
import java.util.Locale

class CourseRepositoryImpl(
    private val api: CoursesApi,
    private val db: AppDatabase
) : CourseRepository {

    private val coursesUrl =
        "https://drive.usercontent.google.com/u/0/uc?id=15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q&export=download"

    override suspend fun getCourses(): Result<List<Course>> {
        return try {
            val response = api.getCourses(coursesUrl)
            val favoriteIds = db.favoriteDao().getAllFavoriteIds().toSet()

            response.courses.filter { it.hasLike && it.id !in favoriteIds }
                .forEach { db.favoriteDao().addToFavorites(FavoriteCourse(it.id)) }

            val updatedFavoriteIds = db.favoriteDao().getAllFavoriteIds().toSet()

            val courses = response.courses.map {
                it.toDomain(isLiked = it.id in updatedFavoriteIds)
            }
            Result.success(courses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleFavorite(courseId: Int, currentlyLiked: Boolean) {
        if (currentlyLiked) {
            db.favoriteDao().removeFromFavorites(courseId)
        } else {
            db.favoriteDao().addToFavorites(FavoriteCourse(courseId))
        }
    }

    override suspend fun getFavoriteCourses(): Result<List<Course>> {
        return try {
            val response = api.getCourses(coursesUrl)
            val favoriteIds = db.favoriteDao().getAllFavoriteIds().toSet()
            val favorites = response.courses
                .filter { it.id in favoriteIds }
                .map { it.toDomain(isLiked = true) }
            Result.success(favorites)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    private fun CourseDto.toDomain(isLiked: Boolean): Course {
        val formattedDate = try {
            val input  = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val output = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
            output.format(input.parse(startDate)!!).replaceFirstChar { it.uppercase() }
        } catch (e: Exception) { startDate }

        return Course(
            id          = id,
            title       = title,
            text        = text,
            price       = "$price ₽",
            rate        = rate,
            startDate   = formattedDate,
            hasLike     = isLiked,
            publishDate = publishDate
        )
    }
}