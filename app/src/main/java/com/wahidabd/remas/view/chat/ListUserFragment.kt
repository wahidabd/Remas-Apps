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
import com.wahidabd.remas.databinding.FragmentListUserBinding
import com.wahidabd.remas.utils.Constants
import com.wahidabd.remas.utils.Loading
import com.wahidabd.remas.utils.MySharedPreferences
import com.wahidabd.remas.utils.quickShowToast
import com.wahidabd.remas.view.chat.adapter.UserListAdapter
import com.wahidabd.remas.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListUserFragment : Fragment() {

    private var _binding: FragmentListUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()
    private lateinit var mAdapter: UserListAdapter
    @Inject
    lateinit var loading: Loading
    @Inject
    lateinit var prefs: MySharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = UserListAdapter()
        binding.rvUser.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
        }

        mAdapter.setOnItemClick {
            val image = if (it.image.isNullOrEmpty()) Constants.STATIC_IMAGE else it.image
            findNavController().navigate(
            ListUserFragmentDirections.actionListUserFragmentToChatRoomFragment(it.id, it.name, image)
        )}

        observable()
    }

    private fun observable() {
        lifecycleScope.launch(Dispatchers.Main){
            viewModel.userList(prefs.getUser().id.toString()).observe(viewLifecycleOwner){res ->
                when(res){
                    is Response.Loading -> {loading.start(requireContext())}
                    is Response.Error -> {
                        loading.stop()
                        quickShowToast(res.e.toString())
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