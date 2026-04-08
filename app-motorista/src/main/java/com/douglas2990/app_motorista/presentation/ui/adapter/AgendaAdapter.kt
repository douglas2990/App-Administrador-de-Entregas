package com.douglas2990.app_motorista.presentation.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.douglas2990.app_motorista.data.repository.model.AgendaDia
import com.douglas2990.app_motorista.databinding.ItemAgendaBinding
import com.example.core.model.Rota

class AgendaAdapter(
    private val onClick: (AgendaDia) -> Unit
) : RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder>() {

    private var listaAgenda = mutableListOf<AgendaDia>()

    fun adicionarLista(lista: List<AgendaDia>) {
        this.listaAgenda = lista.toMutableList()
        notifyDataSetChanged()
    }

    inner class AgendaViewHolder(val binding: ItemAgendaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AgendaDia) {
            // 1. Define a Data (07/04/2026)
            binding.textData.text = item.data

            // 2. Define o Rótulo (Hoje/Amanhã) e a Cor Verde
            binding.textRotulo.text = item.rotulo

            // Lógica simples de cores baseada no que vimos no print
            if (item.rotulo.equals("Hoje", ignoreCase = true)) {
                binding.textRotulo.setTextColor(Color.parseColor("#4CAF50")) // Verde mais forte
            } else {
                binding.textRotulo.setTextColor(Color.parseColor("#8BC34A")) // Verde mais claro/limão
            }

            // 3. Define a Quantidade de Rotas (Badge cinza)
            val sufixo = if (item.quantidadeRotas > 1) "Rotas" else "Rota"
            binding.textQuantidadeRotas.text = "${item.quantidadeRotas} $sufixo"

            // 4. Clique para navegar para o detalhe das rotas daquele dia
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // Certifique-se de que o binding está usando o pacote correto do seu app
        val binding = ItemAgendaBinding.inflate(layoutInflater, parent, false)
        return AgendaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AgendaViewHolder, position: Int) {
        holder.bind(listaAgenda[position])
    }

    override fun getItemCount() = listaAgenda.size
}