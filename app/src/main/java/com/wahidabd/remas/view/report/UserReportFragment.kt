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
import com.wahidabd.remas.databinding.FragmentUserReportBinding
import com.wahidabd.remas.utils.Loading
import com.wahidabd.remas.utils.quickShowToast
import com.wahidabd.remas.view.chat.adapter.UserListAdapter
import com.wahidabd.remas.viewmodel.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserReportFragment : Fragment() {

    private var _binding: FragmentUserReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: UserListAdapter
    private val viewModel: ReportViewModel by viewModels()
    @Inject lateinit var loading: Loading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        mAdapter = UserListAdapter()
        binding.rvUser.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
        }

        lifecycleScope.launch(Dispatchers.Main){
            viewModel.getUser().observe(viewLifecycleOwner){ res ->
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