package com.amrdeveloper.askme.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.RegisterData
import com.amrdeveloper.askme.databinding.FragmentRegisterBinding
import com.amrdeveloper.askme.ui.main.MainViewModel
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.utils.gone
import com.amrdeveloper.askme.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        setupObservers()
        setupListeners()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.getRegisterLiveData().observe(viewLifecycleOwner, {
            binding.loadingBar.gone()
            if(it != null){
                Toast.makeText(requireContext(), "Register Success", Toast.LENGTH_SHORT).show()

                val email: String = binding.emailInputEdit.text.toString()
                val password: String = binding.passInputEdit.text.toString()

                Session.login(requireContext(), email, password, it)

                mainViewModel.updateNavigationButtonVisibility(View.VISIBLE)

                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
            }else{
                Toast.makeText(requireContext(), "Invalid Register", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupListeners() {
        binding.loginTxt.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerButton.setOnClickListener {
            val name = binding.nameInputEdit.text.toString()
            val email = binding.emailInputEdit.text.toString()
            val username = binding.usernameInputEdit.text.toString()
            val password = binding.passInputEdit.text.toString()

            val registerData = RegisterData(name, email, username , password)

            if(registerData.isValidRegisterInfo()){
                viewModel.userRegister(registerData)
                binding.loadingBar.show()
            }else{
                Toast.makeText(requireContext(), "Invalid Information", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}