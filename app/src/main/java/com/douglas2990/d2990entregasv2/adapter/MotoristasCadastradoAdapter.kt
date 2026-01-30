package com.douglas2990.d2990entregasv2.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.douglas2990.d2990entregasv2.databinding.FragmentMotoristasCadastradoAdapterBinding
import com.douglas2990.d2990entregasv2.model.Motorista

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MotoristasCadastradoAdapter(private val list: List<Motorista>,
                                  private var listenner: MotoristaInterface? = null
)
    : RecyclerView.Adapter<MotoristasCadastradoAdapter.MotoristasCadastradoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MotoristasCadastradoViewHolder {

        return MotoristasCadastradoViewHolder(
            FragmentMotoristasCadastradoAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MotoristasCadastradoViewHolder, position: Int) {

        val listMotorista = list[position]
        //val namePokemon = pokemon.name
        val nomeMotorista = listMotorista.nome
        //val pokemonId = pokemon.url.replace("https://pokeapi.co/api/v2/pokemon/", "")



        holder.textViewNomeMotorista.text = nomeMotorista!!.replaceFirstChar(Char::titlecaseChar)





    }

    class MotoristasCadastradoViewHolder(binding: FragmentMotoristasCadastradoAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var textViewNomeMotorista = binding.txtMotorista

    }

    override fun getItemCount(): Int {
        return list.size
    }


}