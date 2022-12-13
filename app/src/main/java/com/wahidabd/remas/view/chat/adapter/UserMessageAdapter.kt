package com.wahidabd.remas.view.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wahidabd.remas.data.response.chat.UserChatGroupResponse
import com.wahidabd.remas.databinding.ItemUserMessageBinding
import com.wahidabd.remas.utils.Constants
import com.wahidabd.remas.utils.setImageUrl

class UserMessageAdapter : RecyclerView.Adapter<UserMessageAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<UserChatGroupResponse>() {
        override fun areItemsTheSame(oldItem: UserChatGroupResponse, newItem: UserChatGroupResponse): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: UserChatGroupResponse, newItem: UserChatGroupResponse): Boolean =
            oldItem == newItem
    }

    private val listDiffer = AsyncListDiffer(this, differCallback)
    var setData: List<UserChatGroupResponse>
        get() = listDiffer.currentList
        set(value) = listDiffer.submitList(value)

    private var listener: ((UserChatGroupResponse) -> Unit)? = null
    fun setOnItemClick(listener: ((UserChatGroupResponse) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMessageAdapter.ViewHolder {
        val binding =
            ItemUserMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserMessageAdapter.ViewHolder, position: Int) {
        holder.bind(setData[position], listener)
    }

    override fun getItemCount(): Int = setData.size

    inner class ViewHolder(private val binding: ItemUserMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserChatGroupResponse, listener: ((UserChatGroupResponse) -> Unit)?) {
            with(binding) {

                val avatar = data.image ?: Constants.STATIC_IMAGE
                imgAvatar.setImageUrl(avatar)
                tvName.text = data.name
                tvMessage.text = data.message
                if (data.unread != 0){
                    tvUnreadMessages.visibility = View.VISIBLE
                    tvUnreadMessages.text = if (data.unread!! > 99) "99+" else data.unread.toString()
                }else tvUnreadMessages.visibility = View.GONE

                viewRoot.setOnClickListener {
                    listener?.let { listener(data) }
                }
            }
        }
    }
}