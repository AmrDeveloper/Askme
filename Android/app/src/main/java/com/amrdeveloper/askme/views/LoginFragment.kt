package com.amrdeveloper.askme.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.databinding.FragmentLoginBinding
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.openFragmentInto
import com.amrdeveloper.askme.extensions.show
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.models.LoginData
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        setupObservers()
        setupListener()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.getSessionLiveData().observe(viewLifecycleOwner, {
            binding.loadingBar.gone()
            if (it != null) {
                val email: String = binding.emailInputEdit.text.str()
                val password: String = binding.passInputEdit.text.str()

                Session.login(requireContext(), email, password, it)

                fragmentManager?.openFragmentInto(R.id.viewContainers, HomeFragment())
            } else {
                Toast.makeText(requireContext(), "Invalid Login", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupListener() {
        binding.registerTxt.setOnClickListener {
            fragmentManager?.openFragmentInto(R.id.viewContainers, RegisterFragment())
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
}