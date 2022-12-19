package com.wahidabd.remas.view.report

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.databinding.FragmentDetailReportBinding
import com.wahidabd.remas.utils.Constants
import com.wahidabd.remas.utils.Loading
import com.wahidabd.remas.utils.quickShowToast
import com.wahidabd.remas.utils.setImageUrl
import com.wahidabd.remas.viewmodel.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DetailReportFragment : Fragment() {

    private var _binding: FragmentDetailReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: ReportAdapter
    @Inject
    lateinit var loading: Loading
    private val args: DetailReportFragmentArgs by navArgs()
    private val viewModel: ReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener { findNavController().navigateUp() }
        binding.tvName.text = args.userName
        binding.imgAvatar.setImageUrl(args.userImage ?: Constants.STATIC_IMAGE)

        mAdapter = ReportAdapter()
        binding.rvDocument.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
        }

        mAdapter.setOnItemClick {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            val uri = Uri.parse(it.file)
            intent.setDataAndType(uri, "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            val newIntent = Intent.createChooser(intent, "Open a file")
            try {
                startActivity(newIntent)
            }catch (e: ActivityNotFoundException){
                quickShowToast("Tidak menemukan aplikasi untuk membuka file ini!")
            }
        }

        setRecyclerAdapter()
    }

    private fun setRecyclerAdapter() {


        lifecycleScope.launch(Dispatchers.Main){
            viewModel.getReportByUser(args.userId.toString()).observe(viewLifecycleOwner){ res ->
                when(res){
                    is Response.Loading -> {loading.start(requireContext())}
                    is Response.Error -> {
                        loading.stop()
                        quickShowToast(res.e.message.toString())
                    }
                    is Response.Success -> {
                        loading.stop()
                        mAdapter.setData = res.data
                    }
                }
            }
        }
    }


}