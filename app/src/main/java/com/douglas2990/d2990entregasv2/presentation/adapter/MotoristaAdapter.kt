package com.douglas2990.d2990entregasv2.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.douglas2990.d2990entregasv2.databinding.MotoristasCadastradosAdapterBinding
import com.douglas2990.d2990entregasv2.model.Motorista

class MotoristaAdapter(
    private val onNovaRotaClick: (Motorista) -> Unit,
    private val onVerRotasClick: (Motorista) -> Unit,
    private val onWhatsappClick: (Motorista) -> Unit
) : ListAdapter<Motorista, MotoristaAdapter.MotoristaViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotoristaViewHolder {
        val binding = MotoristasCadastradosAdapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MotoristaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MotoristaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MotoristaViewHolder(
        private val binding: MotoristasCadastradosAdapterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(motorista: Motorista) {
            binding.textNomeMotorista.text = motorista.nome
            binding.textEmpresaMotorista.text = "Empresa: ${motorista.nomeEmpresa}"
            binding.textTelefoneMotorista.text = motorista.telefone

            // Clique nas ações dos botões
            binding.btnNovaRota.setOnClickListener {
                onNovaRotaClick(motorista)
            }

            binding.btnVerRotas.setOnClickListener {
                onVerRotasClick(motorista)
            }

            // Clique em cima do número do telefone/WhatsApp
            binding.textTelefoneMotorista.setOnClickListener {
                onWhatsappClick(motorista)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Motorista>() {
        override fun areItemsTheSame(oldItem: Motorista, newItem: Motorista): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Motorista, newItem: Motorista): Boolean {
            return oldItem == newItem
        }
    }
}