package com.wahidabd.remas.view.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.databinding.FragmentReportBinding
import com.wahidabd.remas.utils.*
import com.wahidabd.remas.view.chat.adapter.UserListAdapter
import com.wahidabd.remas.viewmodel.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReportViewModel by viewModels()
    private lateinit var mAdapter: UserListAdapter
    private lateinit var reportAdapter: ReportAdapter
    @Inject lateinit var loading: Loading
    @Inject lateinit var prefs: MySharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // check user role
        if (prefs.getUser().role == 1) {
            binding.linearAdmin.visibility = View.GONE
            binding.linearUser.visibility = View.VISIBLE
            setViewUser()
        }else{
            binding.linearAdmin.visibility = View.VISIBLE
            binding.linearUser.visibility = View.GONE
            setViewAdmin()
        }

        binding.relativeAddReport.setOnClickListener {
            findNavController().navigate(
                ReportFragmentDirections.actionReportFragmentToUserReportFragment()
            )
        }

        mAdapter = UserListAdapter()
        binding.rvUser.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
        }

        mAdapter.setOnItemClick {
           findNavController().navigate(
               ReportFragmentDirections.actionReportFragmentToDetailReportFragment(it.id, it.name, it.image)
           )
        }

    }

    private fun setViewUser(){
        binding.tvName.text = prefs.getUser().name
        binding.imgAvatar.setImageUrl(prefs.getUser().image ?: Constants.STATIC_IMAGE)

        // set list
        reportAdapter = ReportAdapter()
        binding.rvDocument.apply {
            adapter = reportAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
        }

        lifecycleScope.launch(Dispatchers.Main){
            viewModel.getReportByUser(prefs.getUser().id.toString()).observe(viewLifecycleOwner){ res ->
                when(res){
                    is Response.Loading -> {loading.start(requireContext())}
                    is Response.Error -> {
                        loading.stop()
                        quickShowToast(res.e.message.toString())
                    }
                    is Response.Success -> {
                        loading.stop()
                        reportAdapter.setData = res.data
                    }
                }
            }
        }
    }

    private fun setViewAdmin(){
        lifecycleScope.launch(Dispatchers.Main){
            viewModel.getUserDocument().observe(viewLifecycleOwner){ res ->
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