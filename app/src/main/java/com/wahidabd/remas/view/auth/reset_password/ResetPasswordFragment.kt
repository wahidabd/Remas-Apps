package com.wahidabd.remas.view.auth.reset_password

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.databinding.FragmentResetPasswordBinding
import com.wahidabd.remas.utils.Constants
import com.wahidabd.remas.utils.Loading
import com.wahidabd.remas.utils.quickShowToast
import com.wahidabd.remas.utils.textToTrim
import com.wahidabd.remas.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()
    @Inject lateinit var loading: Loading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding){
            // back to login page
            tvBackToLogin.setOnClickListener { findNavController().navigateUp() }
            btnReset.setOnClickListener { sendRequest() }
        }
    }

    private fun sendRequest() {
        val email = binding.edtEmail.textToTrim()

        if (email.isEmpty()) quickShowToast(Constants.MESSAGE.EMAIL_IS_NOT_EMPTY)
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) quickShowToast(Constants.MESSAGE.EMAIL_IS_NOT_VALID)
        else observerToViewModel(email)
    }

    private fun observerToViewModel(email: String){
        lifecycleScope.launch(Dispatchers.Main){
            viewModel.resetPassword(email).observe(viewLifecycleOwner){ result ->
                when(result){
                    is Response.Loading -> loading.start(requireContext())
                    is Response.Error -> {
                        loading.stop()
                        quickShowToast(result.e.message.toString())
                    }
                    is Response.Success -> {
                        loading.stop()
                        quickShowToast(result.data.message)
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

}