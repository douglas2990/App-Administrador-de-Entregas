package com.douglas2990.d2990entregasv2.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.douglas2990.d2990entregasv2.databinding.ItemEmpresaBinding
import com.douglas2990.d2990entregasv2.model.Empresa

class EmpresaAdapter(
    private val clickEditar: (Empresa) -> Unit,
    private val clickExcluir: (Empresa) -> Unit
) : RecyclerView.Adapter<EmpresaAdapter.EmpresaViewHolder>() {

    private var listaEmpresas = listOf<Empresa>()

    fun adicionarLista(lista: List<Empresa>) {
        listaEmpresas = lista
        notifyDataSetChanged()
    }

    inner class EmpresaViewHolder(
        val binding: ItemEmpresaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(empresa: Empresa) {
            binding.textNomeEmpresa.text = empresa.nome
            binding.textEmailEmpresa.text = empresa.email
            binding.textCnpjEmpresa.text = empresa.cnpj
            binding.textTelefoneEmpresa.text = empresa.telefone

            // Configura os cliques nos ícones
            binding.btnEdit.setOnClickListener { clickEditar(empresa) }
            binding.btnDelete.setOnClickListener { clickExcluir(empresa) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpresaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemEmpresaBinding.inflate(layoutInflater, parent, false)
        return EmpresaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmpresaViewHolder, position: Int) {
        val empresa = listaEmpresas[position]
        holder.bind(empresa)
    }

    override fun getItemCount() = listaEmpresas.size
}