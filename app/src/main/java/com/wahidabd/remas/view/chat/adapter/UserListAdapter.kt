package com.wahidabd.remas.view.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wahidabd.remas.databinding.ItemUserListBinding
import com.wahidabd.remas.domain.models.User
import com.wahidabd.remas.utils.Constants
import com.wahidabd.remas.utils.setImageUrl

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem
    }

    private val listDiffer = AsyncListDiffer(this, differCallback)
    var setData: List<User>
        get() = listDiffer.currentList
        set(value) = listDiffer.submitList(value)

    private var listener: ((User) -> Unit)? = null
    fun setOnItemClick(listener: ((User) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListAdapter.ViewHolder {
        val binding =
            ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListAdapter.ViewHolder, position: Int) {
        holder.bind(setData[position], listener)
    }

    override fun getItemCount(): Int = setData.size

    inner class ViewHolder(private val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User, listener: ((User) -> Unit)?) {
            with(binding) {

                val avatar = data.image ?: Constants.STATIC_IMAGE
                imgAvatar.setImageUrl(avatar)
                tvName.text = data.name

                viewRoot.setOnClickListener {
                    listener?.let { listener(data) }
                }

            }
        }
    }
}