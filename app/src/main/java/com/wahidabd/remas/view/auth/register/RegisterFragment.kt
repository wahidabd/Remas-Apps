package com.wahidabd.remas.view.auth.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wahidabd.remas.R
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.auth.LoginRequest
import com.wahidabd.remas.data.request.auth.RegisterRequest
import com.wahidabd.remas.databinding.FragmentRegisterBinding
import com.wahidabd.remas.utils.*
import com.wahidabd.remas.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()
    @Inject lateinit var loading: Loading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            loginHere.setOnClickListener { findNavController().navigateUp() }
            btnRegister.setOnClickListener { handleLogin() }
        }

        // call function
        handleShowHidePassword()
    }

    // handle and save text in this fragment
    private fun handleLogin(){
        // get text email and password
        val name = binding.edtName.textToTrim()
        val email = binding.edtEmail.textToTrim()
        val password = binding.edtPassword.textToTrim()
        val confirmPassword = binding.edtConfirmPassword.textToTrim()

        // check while field is empty or not valid
        when{
            name.isEmpty() -> quickShowToast(Constants.MESSAGE.NAME_IS_NOT_EMPTY)
            email.isEmpty() -> quickShowToast(Constants.MESSAGE.EMAIL_IS_NOT_EMPTY)
            password.isEmpty() -> quickShowToast(Constants.MESSAGE.PASSWORD_IS_NOT_EMPTY)
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> quickShowToast(Constants.MESSAGE.EMAIL_IS_NOT_VALID)
            password.length < 8 -> quickShowToast(Constants.MESSAGE.PASSWORD_TO_SHORT)
            password != confirmPassword -> quickShowToast(Constants.MESSAGE.PASSWORD_NOT_MATCH)

            else -> {
                val request = RegisterRequest(name = name, email = email, password = password)
                sendToObservable(request)
            }
        }
    }

    // send request to viewmodel
    private fun sendToObservable(request: RegisterRequest){
        lifecycleScope.launch {
            viewModel.register(request).observe(viewLifecycleOwner) { res ->
                when(res){
                    is Response.Loading -> loading.start(requireContext())
                    is Response.Error -> {
                        loading.stop()
                        quickShowToast(res.e.message.toString())
                    }
                    is Response.Success -> {
                        loading.stop()
                        quickShowToast(res.data.message)
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    // handle show or hide text password
    private fun handleShowHidePassword(){
        var isHide = true
        var isHideConfirm = true

        binding.imgPassword.setOnClickListener {
            if (isHide){
                binding.imgPassword.setImageResource(R.drawable.eye_disable)
                showHidePassword(isHide, binding.edtPassword)
            }else{
                binding.imgPassword.setImageResource(R.drawable.eye_enable)
                showHidePassword(isHide, binding.edtPassword)
            }
            isHide = !isHide
        }

        binding.imgConfirmPassword.setOnClickListener {
            if (isHideConfirm){
                binding.imgConfirmPassword.setImageResource(R.drawable.eye_disable)
                showHidePassword(isHideConfirm, binding.edtConfirmPassword)
            }else{
                binding.imgPassword.setImageResource(R.drawable.eye_enable)
                showHidePassword(isHideConfirm, binding.edtConfirmPassword)
            }
            isHideConfirm = !isHideConfirm
        }
    }
}