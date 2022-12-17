package com.wahidabd.remas.view.report

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wahidabd.remas.R
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.report.ReportRequest
import com.wahidabd.remas.databinding.DialogDocumentBinding
import com.wahidabd.remas.databinding.FragmentCreateReportBinding
import com.wahidabd.remas.utils.*
import com.wahidabd.remas.utils.Constants.REQUEST_CODE_PERMISSIONS
import com.wahidabd.remas.utils.Constants.STORAGE_PERMISSION
import com.wahidabd.remas.viewmodel.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class CreateReportFragment : Fragment() {

    private var _binding: FragmentCreateReportBinding? = null
    private val binding get() = _binding!!

    private val args: CreateReportFragmentArgs by navArgs()
    private val viewModel: ReportViewModel by viewModels()
    private var pdfFile: File? = null
    private var fileName: String? = null

    @Inject lateinit var loading: Loading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionGranted()){
            ActivityCompat.requestPermissions(
                requireActivity(),
                STORAGE_PERMISSION,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addPdf.setOnClickListener { selectPdf() }
        binding.btnSave.setOnClickListener {
            if (!binding.edtBody.text.isNullOrEmpty()) showDialogName()
            else quickShowToast("Komentar harus diisi")
        }

        // set drawable in button
        val image = requireActivity().getDrawable(R.drawable.ic_add_circle_outline)
        binding.addPdf.setCompoundDrawablesWithIntrinsicBounds(image, null, null, null)
        binding.tvName.text = args.userName
        binding.imgAvatar.setImageUrl(args.userImage ?: Constants.STATIC_IMAGE)
    }

    private fun showDialogName(){
        val dialogBinding = DialogDocumentBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_rectangle_blue)
        dialog.setContentView(dialogBinding.root)

        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog.window?.setLayout(width, height)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        dialogBinding.edtFileName.doAfterTextChanged {
            fileName = it.toString()
        }

        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        dialogBinding.btnUpload.setOnClickListener {
            dialog.dismiss()
            sendRequest()
        }
    }

    private fun sendRequest(){
        val body = binding.edtBody.text.toString()
        val request = ReportRequest(
            user_id = args.userId, user_name = args.userName, user_image = args.userImage,
            file_name = fileName, file = pdfFile, body = body
        )

        lifecycleScope.launch(Dispatchers.Main){
            viewModel.createReport(request).observe(viewLifecycleOwner){ res ->
                when(res){
                    is Response.Loading -> {loading.start(requireContext())}
                    is Response.Error -> {
                        loading.stop()
                        quickShowToast(res.e.message.toString())
                    }
                    is Response.Success -> {
                        loading.stop()
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun selectPdf(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "application/pdf"
        val chooser = Intent.createChooser(intent, "choose a file")
        launcherIntentFile.launch(chooser)
    }

    private val launcherIntentFile = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK){
            val selectedFile: Uri = activityResult.data?.data as Uri
            val filePath = selectedFile.path
            val pdfFile = uriToFile(selectedFile, requireContext())
            this.pdfFile = pdfFile

            binding.addPdf.text = filePath
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
}