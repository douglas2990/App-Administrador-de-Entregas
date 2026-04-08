package com.douglas2990.d2990entregasv2.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.douglas2990.d2990entregasv2.R
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

            val context = binding.root.context

            val colorRes: Int
            val iconRes: Int

            when (rota.status) {
                "CONCLUIDA" -> {
                    colorRes = android.R.color.holo_green_dark
                    iconRes = R.drawable.ic_check_circle_24 // Pode usar um check também
                }
                "PROBLEMA" -> {
                    colorRes = android.R.color.holo_red_dark
                    iconRes = R.drawable.ic_warning
                }
                else -> { // PENDENTE
                    colorRes = android.R.color.holo_orange_dark
                    iconRes = R.drawable.ic_delivery
                }
            }
            binding.chipStatus.text = rota.status
            binding.chipStatus.setChipBackgroundColorResource(colorRes)
            binding.imgStatusIcon.setImageResource(iconRes)
            binding.imgStatusIcon.setColorFilter(ContextCompat.getColor(context, colorRes))

            // 2. Exibição da Observação/Motivo (Crucial para o Admin)
            if (rota.status == "PROBLEMA" && !rota.observacao.isNullOrEmpty()) {
                binding.textObservacao.visibility = View.VISIBLE
                binding.textObservacao.text = "Motivo: ${rota.observacao}"
            } else {
                binding.textObservacao.visibility = View.GONE
            }

            //
            //
            //3. Ícone visual rápido (Opcional: se você tiver um ImageView de status no layout)
            binding.imgStatusIcon.setImageResource(
                if(rota.status == "PROBLEMA") R.drawable.ic_warning else R.drawable.ic_delivery
             )


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
