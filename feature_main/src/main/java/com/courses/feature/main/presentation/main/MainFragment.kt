package com.courses.feature.main.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.courses.feature.main.R
import com.courses.feature.main.databinding.FragmentMainBinding
import com.courses.feature.main.presentation.adapter.CoursesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModel()

    private val adapter = CoursesAdapter(
        onFavoriteClick = { course ->
            viewModel.onFavoriteClicked(course)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.sortContainer.setOnClickListener { viewModel.onSortClicked() }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
                is MainState.Content -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.items = state.courses
                    adapter.notifyDataSetChanged()
                }
                is MainState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}