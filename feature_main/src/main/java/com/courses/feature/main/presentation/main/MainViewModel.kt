package com.courses.feature.main.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courses.feature.main.data.model.Course
import com.courses.feature.main.domain.usecase.GetCoursesUseCase
import com.courses.feature.main.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.launch

sealed class MainState {
    object Loading : MainState()
    data class Content(val courses: List<Course>) : MainState()
    data class Error(val message: String) : MainState()
}

class MainViewModel(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state

    private var originalList: List<Course> = emptyList()
    private var isSortedByDate = false

    init { loadCourses() }

    fun loadCourses() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            getCoursesUseCase.execute()
                .onSuccess { courses ->
                    originalList = courses
                    isSortedByDate = false
                    _state.value = MainState.Content(courses)
                }
                .onFailure { e ->
                    _state.value = MainState.Error(e.message ?: "Ошибка загрузки")
                }
        }
    }

    fun onSortClicked() {
        val current = (_state.value as? MainState.Content)?.courses ?: return
        isSortedByDate = !isSortedByDate

        val sorted = if (isSortedByDate) {
            current.sortedByDescending { it.publishDate }
        } else {
            originalList
        }
        _state.value = MainState.Content(sorted)
    }

    fun onFavoriteClicked(course: Course) {
        viewModelScope.launch {
            toggleFavoriteUseCase.execute(course.id, course.hasLike)

            val current = (_state.value as? MainState.Content)?.courses ?: return@launch
            val updated = current.map {
                if (it.id == course.id) it.copy(hasLike = !it.hasLike) else it
            }
            originalList = originalList.map {
                if (it.id == course.id) it.copy(hasLike = !it.hasLike) else it
            }
            _state.value = MainState.Content(updated)
        }
    }
}