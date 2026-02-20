package com.courses.feature.login.presentation.fragment

import com.courses.feature.login.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.courses.feature.login.databinding.FragmentLoginBinding
import com.courses.feature.login.presentation.viewmodel.LoginState
import com.courses.feature.login.presentation.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.net.toUri

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.onLoginClicked(email, password)
        }

        binding.btnVk.setOnClickListener { openInBrowser("https://vk.com/") }
        binding.btnOk.setOnClickListener { openInBrowser("https://ok.ru/") }
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginState.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is LoginState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is LoginState.Loading -> {
                }
                is LoginState.Idle -> Unit
            }
        }
    }

    private fun openInBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}