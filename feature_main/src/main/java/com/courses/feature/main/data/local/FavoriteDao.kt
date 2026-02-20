package com.courses.feature.main.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Query("SELECT id FROM favorite_courses")
    suspend fun getAllFavoriteIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(course: FavoriteCourse)

    @Query("DELETE FROM favorite_courses WHERE id = :courseId")
    suspend fun removeFromFavorites(courseId: Int)
}