package com.douglas2990.d2990entregasv2.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.douglas2990.d2990entregasv2.databinding.ItemDataAgendaBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataAgendaAdapter(
    private val onDataClick: (Long) -> Unit
) : ListAdapter<Long, DataAgendaAdapter.DataViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemDataAgendaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DataViewHolder(
        private val binding: ItemDataAgendaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(timestamp: Long) {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                // 2. FORCE O USO DE UTC AQUI TAMBÉM
                timeZone = java.util.TimeZone.getTimeZone("UTC")
            }



            binding.textDataFormatada.text = sdf.format(Date(timestamp))

            binding.root.setOnClickListener {
                onDataClick(timestamp)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Long>() {
        override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean {
            return oldItem == newItem
        }
    }
}