package com.courses.feature.main.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.courses.feature.main.databinding.FragmentFavoritesBinding
import com.courses.feature.main.presentation.adapter.CoursesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel()

    private lateinit var adapter: CoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CoursesAdapter(
            onFavoriteClick = { course ->
                viewModel.removeFromFavorites(course)
            },
            onCourseClick = { course ->
                val bundle = Bundle().apply {
                    putInt("courseId", course.id)
                    putString("courseTitle", course.title)
                    putString("courseText", course.text)
                    putString("coursePrice", course.price)
                    putString("courseRate", course.rate)
                    putString("courseDate", course.startDate)
                    putBoolean("courseHasLike", course.hasLike)
                }
                val destId = requireContext().resources.getIdentifier(
                    "courseDetailFragment", "id", requireContext().packageName
                )
                findNavController().navigate(destId, bundle)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.courses.observe(viewLifecycleOwner) { courses ->
            adapter.items = courses
            adapter.notifyDataSetChanged()
            binding.tvEmpty.visibility = if (courses.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}