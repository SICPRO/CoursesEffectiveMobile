package com.courses.feature.main.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courses.feature.main.data.model.Course
import com.courses.feature.main.domain.usecase.GetFavoriteCoursesUseCase
import com.courses.feature.main.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteCoursesUseCase: GetFavoriteCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> = _courses

    fun loadFavorites() {
        viewModelScope.launch {
            getFavoriteCoursesUseCase.execute()
                .onSuccess { _courses.value = it }
                .onFailure { _courses.value = emptyList() }
        }
    }

    fun removeFromFavorites(course: Course) {
        viewModelScope.launch {
            toggleFavoriteUseCase.execute(course.id, currentlyLiked = true)
            val updated = _courses.value.orEmpty().filter { it.id != course.id }
            _courses.value = updated
        }
    }
}