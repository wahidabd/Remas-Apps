package com.wahidabd.remas.view.chat

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wahidabd.remas.data.request.chat.ChatRoomRequest
import com.wahidabd.remas.databinding.FragmentChatRoomBinding
import com.wahidabd.remas.utils.Constants
import com.wahidabd.remas.utils.MySharedPreferences
import com.wahidabd.remas.utils.myTimeStamp
import com.wahidabd.remas.utils.setImageUrl
import com.wahidabd.remas.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {

    private var _binding: FragmentChatRoomBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var prefs: MySharedPreferences
    private val args: ChatRoomFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set header of user
        binding.ivAvatar.setImageUrl(args.userImage)
        binding.tvName.text = args.userName
        binding.ivBack.setOnClickListener { findNavController().navigateUp() }

        // hide send button when the edittext is null
        binding.edtMessage.doOnTextChanged { text, _, _, _ ->
            if (text?.isEmpty() == true ||
                text?.isBlank() == true
            ) binding.ivSend.visibility = View.GONE
            else binding.ivSend.visibility = View.VISIBLE
        }

        binding.ivSend.setOnClickListener { sendMessage() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage(){
        val message = binding.edtMessage.text.toString()
        val sender = prefs.getUser()
        val request = ChatRoomRequest(
            sender_id = sender.id.toString(),
            sender_name = sender.name.toString(),
            sender_image = sender.image ?: Constants.STATIC_IMAGE,
            receiver_id = args.userId.toString(),
            receiver_name = args.userName.toString(),
            receiver_image = args.userImage,
            message = message,
            date = myTimeStamp()
        )

        binding.edtMessage.text.clear()
        lifecycleScope.launch(Dispatchers.Main){
            viewModel.sendMessage(request).observe(viewLifecycleOwner){}
        }
    }

}