package com.courses.feature.main.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.courses.feature.main.databinding.FragmentCourseDetailBinding
import com.courses.feature.main.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CourseDetailFragment : Fragment() {

    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!

    private val toggleFavoriteUseCase: ToggleFavoriteUseCase by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val statusBarHeight = insets.getInsets(
                WindowInsetsCompat.Type.statusBars()
            ).top
            val extraMargin = statusBarHeight + (16 * resources.displayMetrics.density).toInt()

            (binding.btnBack.layoutParams as FrameLayout.LayoutParams).apply {
                topMargin = extraMargin
                leftMargin = (16 * resources.displayMetrics.density).toInt()
            }
            (binding.btnBookmark.layoutParams as FrameLayout.LayoutParams).apply {
                topMargin = extraMargin
                rightMargin = (16 * resources.displayMetrics.density).toInt()
            }

            binding.btnBack.requestLayout()
            binding.btnBookmark.requestLayout()

            insets
        }

        val courseId = arguments?.getInt("courseId") ?: 0
        val title    = arguments?.getString("courseTitle") ?: ""
        val text     = arguments?.getString("courseText")  ?: ""
        val rate     = arguments?.getString("courseRate")  ?: ""
        val date     = arguments?.getString("courseDate")  ?: ""
        var isLiked  = arguments?.getBoolean("courseHasLike") ?: false

        binding.tvTitle.text       = title
        binding.tvDescription.text = text
        binding.tvRate.text        = rate
        binding.tvDate.text        = date

        val colors = listOf("#E8A040", "#C4967A", "#6B8CAE", "#7DAE6B", "#AE6B8C")
        binding.ivCover.setBackgroundColor(
            android.graphics.Color.parseColor(
                colors[(title.hashCode() and 0x7FFFFFFF) % colors.size]
            )
        )

        updateBookmarkTint(isLiked)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnBookmark.setOnClickListener {
            isLiked = !isLiked
            updateBookmarkTint(isLiked)
            lifecycleScope.launch {
                toggleFavoriteUseCase.execute(courseId, !isLiked)
            }
        }
    }

    private fun updateBookmarkTint(isLiked: Boolean) {
        val color = if (isLiked) "#12B956" else "#151515"
        binding.btnBookmark.setColorFilter(android.graphics.Color.parseColor(color))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}