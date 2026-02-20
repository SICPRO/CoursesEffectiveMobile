package com.courses.feature.main.presentation.adapter

import coil.load // для image

import com.courses.feature.main.data.model.Course
import com.courses.feature.main.databinding.ItemCourseBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun courseAdapterDelegate(
    onFavoriteClick: (Course) -> Unit,
    onCourseClick: (Course) -> Unit
) = adapterDelegateViewBinding<Course, Course, ItemCourseBinding>(
    { layoutInflater, parent -> ItemCourseBinding.inflate(layoutInflater, parent, false) }
) {
    binding.ivBookmark.setOnClickListener { onFavoriteClick(item) }
    binding.root.setOnClickListener { onCourseClick(item) }
    binding.tvMore.setOnClickListener { onCourseClick(item) }

    bind {
        binding.tvTitle.text       = item.title
        binding.tvDescription.text = item.text
        binding.tvPrice.text       = item.price
        binding.tvRate.text        = item.rate
        binding.tvDate.text        = item.startDate

        val tint = if (item.hasLike) 0xFF12B956.toInt() else 0xFFF2F2F3.toInt()
        binding.ivBookmark.setColorFilter(tint)

        // Цветная заглушка вместо картинки (картинок в JSON нет)
        val colors = listOf("#E8A040", "#C4967A", "#6B8CAE", "#7DAE6B", "#AE6B8C")
        binding.ivCover.setBackgroundColor(
            android.graphics.Color.parseColor(colors[adapterPosition % colors.size])
        )
    }
}