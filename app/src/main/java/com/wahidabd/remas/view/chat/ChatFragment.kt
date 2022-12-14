package com.wahidabd.remas.view.chat

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
import com.wahidabd.remas.databinding.FragmentChatBinding
import com.wahidabd.remas.utils.Constants
import com.wahidabd.remas.utils.Loading
import com.wahidabd.remas.utils.MySharedPreferences
import com.wahidabd.remas.utils.quickShowToast
import com.wahidabd.remas.view.chat.adapter.UserMessageAdapter
import com.wahidabd.remas.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels()
    @Inject lateinit var loading: Loading
    @Inject lateinit var prefs: MySharedPreferences

    private lateinit var mAdapter: UserMessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = UserMessageAdapter()
        binding.rvUser.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
        }

        // check user role
        if (prefs.getUser().role == 1) binding.relativeAddChat.visibility = View.GONE
        else binding.relativeAddChat.visibility = View.VISIBLE

        binding.imgAddChat.setOnClickListener {
            findNavController().navigate(
                ChatFragmentDirections.actionChatFragmentToListUserFragment()
            )
        }

        mAdapter.setOnItemClick {
            findNavController().navigate(
                ChatFragmentDirections.actionChatFragmentToChatRoomFragment(it.id, it.name, it.image ?: Constants.STATIC_IMAGE)
            )
        }

        Timber.d("USER ID -> ${prefs.getUser().id}")
        observable()
    }

    private fun observable() {
        lifecycleScope.launch(Dispatchers.Main){
            viewModel.getGroupChat(prefs.getUser().id.toString()).observe(viewLifecycleOwner){ res ->
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