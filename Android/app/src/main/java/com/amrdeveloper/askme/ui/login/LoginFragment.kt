package com.amrdeveloper.askme.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.LoginData
import com.amrdeveloper.askme.databinding.FragmentLoginBinding
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.utils.gone
import com.amrdeveloper.askme.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!


    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        setupObservers()
        setupListener()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.getSessionLiveData().observe(viewLifecycleOwner, {
            binding.loadingBar.gone()
            if (it != null) {
                val email: String = binding.emailInputEdit.text.toString()
                val password: String = binding.passInputEdit.text.toString()

                Session.login(requireContext(), email, password, it)
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Invalid Login", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupListener() {
        binding.registerTxt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener {
            val email: String = binding.emailInputEdit.text.toString()
            val password: String = binding.passInputEdit.text.toString()
            val loginData = LoginData(email, password)

            if (loginData.isValidLoginInfo()) {
                viewModel.userLogin(loginData)
                binding.loadingBar.show()
                return@setOnClickListener
            }

            if (loginData.isValidEmail().not()) {
                binding.emailInputLayout.error = "Invalid Email"
            }

            if (loginData.isValidPassword().not()) {
                binding.passInputLayout.error = "Invalid Password"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}