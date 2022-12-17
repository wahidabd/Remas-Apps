package com.wahidabd.remas.view.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wahidabd.remas.data.response.ReportDocumentResponse
import com.wahidabd.remas.databinding.ItemReportDocumentBinding
import com.wahidabd.remas.databinding.ItemUserListBinding
import com.wahidabd.remas.domain.models.User
import com.wahidabd.remas.utils.Constants
import com.wahidabd.remas.utils.setImageUrl

class ReportAdapter : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ReportDocumentResponse>() {
        override fun areItemsTheSame(oldItem: ReportDocumentResponse, newItem: ReportDocumentResponse): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ReportDocumentResponse, newItem: ReportDocumentResponse): Boolean =
            oldItem == newItem
    }

    private val listDiffer = AsyncListDiffer(this, differCallback)
    var setData: List<ReportDocumentResponse>
        get() = listDiffer.currentList
        set(value) = listDiffer.submitList(value)

    private var listener: ((ReportDocumentResponse) -> Unit)? = null
    fun setOnItemClick(listener: ((ReportDocumentResponse) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportAdapter.ViewHolder {
        val binding =
            ItemReportDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportAdapter.ViewHolder, position: Int) {
        holder.bind(setData[position], listener)
    }

    override fun getItemCount(): Int = setData.size

    inner class ViewHolder(private val binding: ItemReportDocumentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ReportDocumentResponse, listener: ((ReportDocumentResponse) -> Unit)?) {
            with(binding) {
                binding.fileName.text = data.file_name
            }
        }
    }
}