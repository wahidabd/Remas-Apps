package com.wahidabd.remas.view.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.ProfileRequest
import com.wahidabd.remas.databinding.FragmentProfileBinding
import com.wahidabd.remas.utils.*
import com.wahidabd.remas.utils.Constants.REQUEST_CODE_PERMISSIONS
import com.wahidabd.remas.utils.Constants.STORAGE_PERMISSION
import com.wahidabd.remas.view.auth.AuthActivity
import com.wahidabd.remas.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var loading: Loading
    @Inject lateinit var prefs: MySharedPreferences
    private var myFile: File? = null
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = prefs.getUser()
        val age = if (user.age == 0) "-" else user.age.toString()
        binding.ivAvatar.setImageUrl(user.image ?: Constants.STATIC_IMAGE)
        binding.edtName.setText(user.name)
        binding.edtAge.setText(age)
        binding.edtAddress.setText(user.address)
        binding.edtPhone.setText(user.phone)

        if (!allPermissionGranted()){
            ActivityCompat.requestPermissions(
                requireActivity(),
                STORAGE_PERMISSION,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.ivAvatar.setOnClickListener { selectImage() }
        binding.ivLogout.setOnClickListener { logout() }
        binding.btnSave.setOnClickListener { sendRequest() }
    }

    private fun sendRequest(){
        val name = binding.edtName.text.toString()
        val age = binding.edtAge.text.toString()
        val address = binding.edtAddress.text.toString()
        val phone = binding.edtPhone.text.toString()

        if (name.isEmpty() || address.isEmpty() || age.isEmpty() || phone.isEmpty()){
            quickShowToast("Form tidak boleh kosong")
        }else{
            val request = ProfileRequest(
                id = prefs.getUser().id.toString(),
                file = myFile, name = name, age = age.toInt(),
                address = address, phone = phone, email = prefs.getUser().email.toString()
            )

            lifecycleScope.launch(Dispatchers.Main){
                viewModel.editProfile(request).observe(viewLifecycleOwner){ res ->
                    when(res){
                        is Response.Loading -> {loading.start(requireContext())}
                        is Response.Error -> {
                            loading.stop()
                            quickShowToast(res.e.message.toString())
                        }
                        is Response.Success -> {
                            loading.stop()
                            prefs.setUser(res.data)
                        }
                    }
                }
            }
        }
    }

    private fun selectImage(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "choose a picture")
        launcherIntentFile.launch(chooser)
    }

    @SuppressLint("SetTextI18n")
    private val launcherIntentFile = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK){
            val selectedFile: Uri = activityResult.data?.data as Uri
            val filePath = selectedFile.path
            val myFile = uriToFileImage(selectedFile, requireContext())
            this.myFile = myFile

            // set name to textview=
            binding.ivAvatar.setImageURI(selectedFile)
            binding.ivAvatar.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (!allPermissionGranted()){
                quickShowToast("Not getting permission!")
                requireActivity().finish()
            }
        }
    }

    private fun  allPermissionGranted() = STORAGE_PERMISSION.all { permission ->
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun logout(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Kaluar")
            .setMessage("Yakin ingin keluar dari aplikasi?")
            .setPositiveButton("YA"){_, _ ->
                prefs.logout()
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("BATAL"){d, _ ->
                d.dismiss()
            }
            .show()
    }

}