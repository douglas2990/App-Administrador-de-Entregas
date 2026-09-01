package com.douglas2990.app_motorista.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.douglas2990.app_motorista.databinding.ItemObservacaoBinding
import com.example.core.model.ObservacaoCliente
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ObservacoesAdapter : RecyclerView.Adapter<ObservacoesAdapter.ObservacaoViewHolder>() {

    private var lista = listOf<ObservacaoCliente>()

    fun adicionarLista(novaLista: List<ObservacaoCliente>) {
        lista = novaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservacaoViewHolder {
        val binding = ItemObservacaoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ObservacaoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ObservacaoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount() = lista.size

    inner class ObservacaoViewHolder(private val binding: ItemObservacaoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(observacao: ObservacaoCliente) {
            binding.textNomeCliente.text = "Cliente: ${observacao.nomeCliente}"
            binding.textEndereco.text = "Endereço: ${observacao.endereco}"
            binding.textRelato.text = observacao.relato
            binding.textMotorista.text = "Por: ${observacao.nomeMotorista}"
            
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val dataFormatada = sdf.format(Date(observacao.dataCriacao))
            binding.textData.text = dataFormatada
        }
    }
}
