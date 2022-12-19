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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.wahidabd.remas.core.Response
import com.wahidabd.remas.data.request.chat.ChatRoomRequest
import com.wahidabd.remas.databinding.FragmentChatRoomBinding
import com.wahidabd.remas.utils.*
import com.wahidabd.remas.view.chat.adapter.ChatRoomAdapter
import com.wahidabd.remas.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {

    private var _binding: FragmentChatRoomBinding? = null
    private val binding get() = _binding!!

    // set read chat from fragment
    private lateinit var stateListener: ValueEventListener
    private lateinit var resetStateListener: ValueEventListener

    @Inject lateinit var prefs: MySharedPreferences
    @Inject lateinit var loading: Loading
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
        readMessage()
    }

    private fun readMessage(){
        val mAdapter = ChatRoomAdapter(prefs.getUser().id.toString())
        binding.rvChat.apply {
            adapter = mAdapter
            val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            manager.stackFromEnd = true
            layoutManager = manager
            itemAnimator = DefaultItemAnimator()
        }

        lifecycleScope.launch(Dispatchers.Main){
            viewModel.readMessage(prefs.getUser().id.toString(), args.userId.toString()).observe(viewLifecycleOwner){ res ->
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

    private val dbReference: DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.TABLE.CHAT)
    override fun onStart() {
        super.onStart()
        stateListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    i.child("read").ref.setValue(true)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }

        dbReference.child(args.userId.toString()).child(prefs.getUser().id.toString())
            .child(Constants.TABLE.CHAT_ROOM).addValueEventListener(stateListener)

        resetStateListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child("unread").ref.setValue(0)
            }
            override fun onCancelled(error: DatabaseError) {}
        }

        dbReference.child(prefs.getUser().id.toString()).child(args.userId.toString())
            .addValueEventListener(resetStateListener)
    }

    override fun onStop() {
        super.onStop()
        dbReference.child(args.userId.toString()).child(prefs.getUser().id.toString())
            .child(Constants.TABLE.CHAT_ROOM).removeEventListener(stateListener)

        dbReference.child(prefs.getUser().id.toString()).child(args.userId.toString())
            .removeEventListener(resetStateListener)
    }
}