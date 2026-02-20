package com.courses.feature.main.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_courses")
data class FavoriteCourse(
    @PrimaryKey val id: Int
)