//package com.wahidabd.remas.view.chat.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//
//class ChatRoomAdapter(private val id: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    companion object {
//        private const val MESSAGE_TYPE_LEFT = 0
//        private const val MESSAGE_TYPE_RIGHT = 1
//    }
//
//    private val differCallback = object : DiffUtil.ItemCallback<ChatRoomResponse>(){
//        override fun areItemsTheSame(oldItem: ChatRoomResponse, newItem: ChatRoomResponse): Boolean =
//            oldItem == newItem
//
//        override fun areContentsTheSame(oldItem: ChatRoomResponse, newItem: ChatRoomResponse): Boolean =
//            oldItem == newItem
//    }
//
//    private val listDiffer = AsyncListDiffer(this, differCallback)
//    var setData: List<ChatRoomResponse>
//        get() = listDiffer.currentList
//        set(value) = listDiffer.submitList(value)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == MESSAGE_TYPE_LEFT){
//            val binding = ItemChatLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            LeftViewHolder(binding)
//        }else{
//            val binding = ItemChatRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            RightViewHolder(binding)
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val item = setData[position]
//        when(holder){
//            is LeftViewHolder -> {holder.bind(item)}
//            is RightViewHolder -> {holder.bind(item)}
//        }
//    }
//
//    override fun getItemCount(): Int = setData.size
//
//    class LeftViewHolder(private val binding: ItemChatLeftBinding) : RecyclerView.ViewHolder(binding.root){
//        fun bind(data: ChatRoomResponse){
//            binding.tvMessage.text = data.message
//            binding.tvDateChat.text = DateTimeUtil.getDescriptiveMessageDateTime(data.date.toString(), true)
//        }
//    }
//
//    class RightViewHolder(private val binding: ItemChatRightBinding) : RecyclerView.ViewHolder(binding.root){
//        fun bind(data: ChatRoomResponse){
//            binding.tvMessage.text = data.message
//            binding.tvDateChat.text = DateTimeUtil.getDescriptiveMessageDateTime(data.date.toString(), true)
//
//            if (data.read == false) binding.ivChatStatus.setImageDrawable(R.drawable.ic_chat_unread)
//            else binding.ivChatStatus.setImageDrawable(R.drawable.ic_chat_read)
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (setData[position].sender_id == id){
//            MESSAGE_TYPE_RIGHT
//        }else{
//            MESSAGE_TYPE_LEFT
//        }
//    }
//
//}