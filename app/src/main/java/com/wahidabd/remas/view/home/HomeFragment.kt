package com.wahidabd.remas.view.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahidabd.remas.R
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.databinding.FragmentHomeBinding
import com.wahidabd.remas.utils.*
import com.wahidabd.remas.view.chat.adapter.UserListAdapter
import com.wahidabd.remas.view.chat.adapter.UserMessageAdapter
import com.wahidabd.remas.view.report.ReportAdapter
import com.wahidabd.remas.viewmodel.ChatViewModel
import com.wahidabd.remas.viewmodel.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatAdapter: UserMessageAdapter
    private lateinit var docAdapter: ReportAdapter
    private lateinit var reportAdapter: UserListAdapter
    private val chatViewModel: ChatViewModel by viewModels()
    private val docViewMode: ReportViewModel by viewModels()
    @Inject lateinit var prefs: MySharedPreferences
    @Inject lateinit var loading: Loading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivAvatar.setImageUrl(prefs.getUser().image ?: Constants.STATIC_IMAGE)
        binding.tvName.text = prefs.getUser().name

        chatAdapter = UserMessageAdapter()
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
        }

        chatAdapter.setOnItemClick {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToChatRoomFragment(it.id, it.name, it.image ?: Constants.STATIC_IMAGE)
            )
        }

        if (prefs.getUser().role == 1) {
            docAdapter = ReportAdapter()
            binding.rvDocument.apply {
                adapter = docAdapter
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
            }
            docAdapter.setOnItemClick {
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
            documentAdapterUser()
        } else {
            reportAdapter = UserListAdapter()
            binding.rvDocument.apply {
                adapter = reportAdapter
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
            }
            reportAdapterAdmin()
            reportAdapter.setOnItemClick {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailReportFragment(it.id, it.name, it.image)
                )
            }
        }

        chatAdapter()
    }

    private fun reportAdapterAdmin(){
        binding.rvDocument.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.Main){
            docViewMode.getUserDocument().observe(viewLifecycleOwner){ res ->
                if (res is Response.Success){
                    if (res.data.isNotEmpty())  reportAdapter.setData = if (res.data.size < 3) res.data else res.data.take(3)
                    else binding.rvDocument.visibility = View.GONE
                }
            }
        }
    }

    private fun documentAdapterUser() {
        binding.rvDocument.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.Main){
            docViewMode.getReportByUser(prefs.getUser().id.toString()).observe(viewLifecycleOwner){ res ->
                if(res is Response.Success){
                    if (res.data.isNotEmpty()) {
                        docAdapter.setData = if (res.data.size < 3) res.data else res.data.take(3)
                        binding.rvDocument.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_rectangle_white_12)
                        binding.rvDocument.setPadding(14, 8, 14, 8)
                    }
                    else binding.rvDocument.visibility = View.GONE
                }
            }
        }
    }

    private fun chatAdapter(){
        lifecycleScope.launch(Dispatchers.Main){
            chatViewModel.getGroupChat(prefs.getUser().id.toString()).observe(viewLifecycleOwner){ res ->
                when(res){
                    is Response.Loading -> {loading.start(requireContext())}
                    is Response.Error -> {
                        loading.stop()
                        quickShowToast(res.e.message.toString())
                    }
                    is Response.Success -> {
                        loading.stop()
                        chatAdapter.setData = if (res.data.size < 3) res.data else res.data.take(3)
                    }
                }
            }
        }
    }

}