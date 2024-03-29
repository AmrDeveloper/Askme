package com.amrdeveloper.askme.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.themeList
import com.amrdeveloper.askme.databinding.FragmentSettingsBinding
import com.amrdeveloper.askme.ui.adapter.ColorGridAdapter
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.utils.Validation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        setupObservers()
        setupListeners()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.themeColor.observe(viewLifecycleOwner, {
            Session.updateColor(requireContext(), it)
        })

        viewModel.messages.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupListeners() {
        binding.changeStatus.setOnClickListener { changeStatusAction() }
        binding.changeColor.setOnClickListener { changeColorAction() }
        binding.changeLocation.setOnClickListener { changeLocationAction() }
        binding.changePassword.setOnClickListener { changePasswordAction() }
    }

    private fun changeStatusAction() {
        val dialogView = layoutInflater.inflate(R.layout.change_status_dialog, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Change Status")
        dialogBuilder.setCancelable(true)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val changeButton: Button = dialogView.findViewById(R.id.changeButton)
        val statusEdit : EditText = dialogView.findViewById(R.id.statusEdit)

        changeButton.setOnClickListener {
            val statusTxt = statusEdit.text.toString()
            if(statusTxt.isEmpty()){
                Toast.makeText(requireContext(), "Invalid Status", Toast.LENGTH_SHORT)
                    .show()
            }else{
                val token = Session.getHeaderToken(requireContext())
                val userId = Session.getUserId(requireContext()).toString()
                viewModel.changeUserStatus(token, userId, statusTxt)
            }

            alertDialog.dismiss()
        }
        dialogBuilder.show()
    }

    private fun changeLocationAction() {
        val dialogView = layoutInflater.inflate(R.layout.change_location_dialog, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Change Location")
        dialogBuilder.setCancelable(true)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val changeButton: Button = dialogView.findViewById(R.id.changeButton)
        val locationEdit : EditText = dialogView.findViewById(R.id.locationEdit)

        changeButton.setOnClickListener {
            val locationTxt = locationEdit.text.toString()
            if(locationTxt.isEmpty()){
                Toast.makeText(requireContext(), "Invalid Location", Toast.LENGTH_SHORT)
                    .show()
            }else{
                val token = Session.getHeaderToken(requireContext())
                val userId = Session.getUserId(requireContext()).toString()
                viewModel.changeUserLocation(token, userId, locationTxt)
            }

            alertDialog.dismiss()
        }
        dialogBuilder.show()
    }

    private fun changeColorAction() {
        val dialogView = layoutInflater.inflate(R.layout.change_color_dialog, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Change App Theme")
        dialogBuilder.setCancelable(true)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val colorGridView : GridView = dialogView.findViewById(R.id.colorGridView)

        val colorGridAdapter =
            ColorGridAdapter(requireContext(), themeList)
        colorGridView.adapter = colorGridAdapter

        colorGridView.setOnItemClickListener {_, _, position, _ ->
            val themeName = colorGridAdapter.getItem(position)!!.themeColor

            val token = Session.getHeaderToken(requireContext())
            val userId = Session.getUserId(requireContext()).toString()

            viewModel.changeUserColor(token, userId, themeName)

            alertDialog.cancel()
        }

        dialogBuilder.show()
    }

    private fun changePasswordAction() {
        val dialogView = layoutInflater.inflate(R.layout.change_password_dialog, null)

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Change Password")
        dialogBuilder.setCancelable(true)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val changeButton: Button = dialogView.findViewById(R.id.changeButton)
        val oldPassword: EditText = dialogView.findViewById(R.id.oldPasswordEdit)
        val newPassword: EditText = dialogView.findViewById(R.id.newPasswordEdit)

        changeButton.setOnClickListener {
            val oldPasswordTxt = oldPassword.text.toString()
            if (Session.getUserPassword(requireContext()) == oldPasswordTxt) {
                val newPasswordTxt = newPassword.text.toString()
                if(oldPasswordTxt == newPasswordTxt || Validation.isValidPassword(newPasswordTxt).not()){
                    Toast.makeText(requireContext(), "New password is equal old password", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    val token = Session.getHeaderToken(requireContext())
                    val userId = Session.getUserId(requireContext()).toString()
                    viewModel.changeUserPassword(token, userId, newPasswordTxt)
                }
            } else {
                Toast.makeText(requireContext(), "Invalid Old Password", Toast.LENGTH_SHORT)
                    .show()
            }
            alertDialog.dismiss()
        }

        dialogBuilder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}