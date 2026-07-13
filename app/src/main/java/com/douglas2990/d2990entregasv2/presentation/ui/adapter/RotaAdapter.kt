package com.douglas2990.d2990entregasv2.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.douglas2990.d2990entregasv2.R
import com.douglas2990.d2990entregasv2.databinding.ItemRotaBinding
import com.douglas2990.d2990entregasv2.model.Rota

class RotaAdapter(
    private val onItemClick: (Rota) -> Unit,
    private val onEditarClick: (Rota) -> Unit,
    private val onDeletarClick: (Rota) -> Unit
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

            val context = binding.root.context

            // 1. Configuração visual dinâmica de Status
            val colorRes: Int
            val iconRes: Int

            when (rota.status) {
                "CONCLUIDA" -> {
                    colorRes = android.R.color.holo_green_dark
                    iconRes = R.drawable.ic_check_circle_24
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

            // 🚀 3. Menu de Contexto Profissional (Editar/Deletar)
            binding.btnMenuContexto.setOnClickListener { view ->
                val popup = PopupMenu(context, view)
                popup.menuInflater.inflate(R.menu.menu_rota_opcoes, popup.menu)

                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_editar -> {
                            onEditarClick(rota)
                            true
                        }
                        R.id.menu_deletar -> {
                            onDeletarClick(rota)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }

            // Clique simples no card
            binding.root.setOnClickListener { onItemClick(rota) }
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