package com.courses.feature.main.presentation.adapter

import com.courses.feature.main.data.model.Course
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class CoursesAdapter(
    onFavoriteClick: (Course) -> Unit,
    onCourseClick: (Course) -> Unit
) : ListDelegationAdapter<List<Course>>(
    courseAdapterDelegate(onFavoriteClick, onCourseClick)
)