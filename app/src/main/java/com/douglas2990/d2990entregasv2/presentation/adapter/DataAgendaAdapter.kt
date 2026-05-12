package com.douglas2990.d2990entregasv2.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.douglas2990.d2990entregasv2.R
import com.douglas2990.d2990entregasv2.databinding.ItemDataAgendaBinding
import com.douglas2990.d2990entregasv2.model.ItemAgendaAdmin
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataAgendaAdapter(
    private val onDataClick: (Long) -> Unit,
    private val onArquivarClick: (Long) -> Unit
) : ListAdapter<ItemAgendaAdmin, DataAgendaAdapter.DataViewHolder>(DiffCallback) {

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

        fun bind(item: ItemAgendaAdmin) {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                timeZone = java.util.TimeZone.getTimeZone("UTC")
            }
            binding.textDataFormatada.text = sdf.format(Date(item.data))

            // Lógica de Cores e Ícones
            when {
                item.temPendente -> {
                    binding.btnAcao.setImageResource(R.drawable.ic_play_arrow)
                    binding.btnAcao.setColorFilter(Color.parseColor("#FF9800")) // Laranja
                }
                item.temProblema -> {
                    binding.btnAcao.setImageResource(R.drawable.ic_archive)
                    binding.btnAcao.setColorFilter(Color.parseColor("#F44336")) // Vermelho
                }
                else -> {
                    binding.btnAcao.setImageResource(R.drawable.ic_archive)
                    binding.btnAcao.setColorFilter(Color.parseColor("#4CAF50")) // Verde
                }
            }

            binding.root.setOnClickListener {
                onDataClick(item.data)
            }

            binding.btnAcao.setOnClickListener {
                if (item.temPendente) {
                    onDataClick(item.data)
                } else {
                    onArquivarClick(item.data)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ItemAgendaAdmin>() {
        override fun areItemsTheSame(oldItem: ItemAgendaAdmin, newItem: ItemAgendaAdmin): Boolean {
            // Compara pela data (que é o identificador único do dia)
            return oldItem.data == newItem.data
        }

        override fun areContentsTheSame(oldItem: ItemAgendaAdmin, newItem: ItemAgendaAdmin): Boolean {
            // Compara o objeto inteiro para detectar mudanças nos status (pendente/problema)
            return oldItem == newItem
        }
    }
}