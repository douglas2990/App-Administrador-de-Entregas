package com.douglas2990.d2990entregasv2.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.douglas2990.d2990entregasv2.databinding.ItemRotaBinding
import com.douglas2990.d2990entregasv2.model.Rota

class RotaAdapter(
    private val onItemClick: (Rota) -> Unit,
    private val onItemLongClick: (Rota) -> Unit
) : ListAdapter<Rota, RotaAdapter.RotaViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RotaViewHolder {
        val binding = ItemRotaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RotaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RotaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RotaViewHolder(private val binding: ItemRotaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rota: Rota) {
            binding.textOS.text = "OS: ${rota.os}"
            binding.textEmpresaDestino.text = rota.nomeEmpresaDestino
            binding.textEndereco.text = rota.endereco
            binding.textMotorista.text = "Motorista: ${rota.nomeMotorista}"
            binding.chipStatus.text = rota.status

            val color = when (rota.status) {
                "CONCLUIDA" -> android.R.color.holo_green_dark
                "PROBLEMA" -> android.R.color.holo_red_dark
                else -> android.R.color.holo_orange_dark
            }
            binding.chipStatus.setChipBackgroundColorResource(color)

            binding.root.setOnClickListener { onItemClick(rota) }
            binding.root.setOnLongClickListener {
                onItemLongClick(rota)
                true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Rota>() {
        override fun areItemsTheSame(oldItem: Rota, newItem: Rota): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Rota, newItem: Rota): Boolean {
            return oldItem == newItem
        }
    }
}
