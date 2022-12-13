package com.wahidabd.remas.view.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wahidabd.remas.databinding.FragmentReportBinding
import com.wahidabd.remas.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

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
        if (prefs.getUser().role == 1) binding.relativeAddReport.visibility = View.GONE
        else binding.relativeAddReport.visibility = View.VISIBLE

        binding.relativeAddReport.setOnClickListener {
            findNavController().navigate(
                ReportFragmentDirections.actionReportFragmentToUserReportFragment()
            )
        }

    }

}