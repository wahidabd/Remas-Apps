package com.wahidabd.remas.view.auth.login

import android.content.Intent
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
import com.wahidabd.remas.databinding.FragmentLoginBinding
import com.wahidabd.remas.utils.*
import com.wahidabd.remas.view.MainActivity
import com.wahidabd.remas.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()
    @Inject lateinit var loading: Loading
    @Inject lateinit var prefs: MySharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            // to register page
            registerHere.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
            }

            // to reset password page
            tvForgotPassword.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment()
                findNavController().navigate(action)
            }

            // call function handle login
            btnLogin.setOnClickListener { handleLogin() }
        }

        // call function
        handleShowHidePassword()
    }

    // handle and save text in this fragment
    private fun handleLogin(){
        // get text email and password
        val email = binding.edtEmail.textToTrim()
        val password = binding.edtPassword.textToTrim()

        // check while field is empty or not valid
        when{
            email.isEmpty() -> quickShowToast(Constants.MESSAGE.EMAIL_IS_NOT_EMPTY)
            password.isEmpty() -> quickShowToast(Constants.MESSAGE.PASSWORD_IS_NOT_EMPTY)
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> quickShowToast(Constants.MESSAGE.EMAIL_IS_NOT_VALID)
            password.length < 8 -> quickShowToast(Constants.MESSAGE.PASSWORD_TO_SHORT)

            else -> {
                val request = LoginRequest(email, password)
                sendToObservable(request)
            }
        }
    }

    // send request to viewmodel
    private fun sendToObservable(request: LoginRequest){
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.login(request).observe(viewLifecycleOwner) { res ->
                when(res){
                    is Response.Loading -> loading.start(requireContext())
                    is Response.Error -> {
                        loading.stop()
                        quickShowToast(res.e.message.toString())
                    }
                    is Response.Success -> {
                        loading.stop()
                        Timber.d("DATA: ${res.data}")

                        // set to preference
                        prefs.setLogin(true)
                        prefs.setUser(res.data)

                        // go to main activity
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        activity?.finish()
                    }
                }
            }
        }
    }

    // handle show or hide text password
    private fun handleShowHidePassword(){
        var isHide = true

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
    }

}