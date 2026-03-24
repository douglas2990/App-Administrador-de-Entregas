package com.douglas2990.d2990entregasv2.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.douglas2990.d2990entregasv2.databinding.FragmentRotasDoMotoristaBinding
import com.douglas2990.d2990entregasv2.model.Motorista
import com.douglas2990.d2990entregasv2.model.Rota
import com.douglas2990.d2990entregasv2.presentation.ui.adapter.RotaAdapter
import com.douglas2990.d2990entregasv2.presentation.viewmodel.RotaViewModel
import com.example.core.AlertaCarregamento
import com.example.core.UIstatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RotasDoMotoristaFragment : Fragment() {

    private var _binding: FragmentRotasDoMotoristaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RotaViewModel by viewModels()
    private lateinit var rotaAdapter: RotaAdapter
    private var motorista: Motorista? = null
    
    private val alertaCarregamento by lazy { AlertaCarregamento(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            motorista = it.getParcelable("motorista")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRotasDoMotoristaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        motorista?.let {
            binding.textTitulo.text = "Rotas de ${it.nome}"
            viewModel.listarRotasMotorista(it.id)
        } ?: run {
            Toast.makeText(context, "Motorista não encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        rotaAdapter = RotaAdapter(
            onItemClick = { rota ->
                if (rota.status == "CONCLUIDA") {
                    Toast.makeText(context, "Entrega Concluída!", Toast.LENGTH_SHORT).show()
                }
            },
            onItemLongClick = { rota ->
                exibirDialogoConfirmacao(rota)
            }
        )

        binding.rvRotas.apply {
            adapter = rotaAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {
        viewModel.rotas.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is UIstatus.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    val lista = status.dados ?: emptyList()
                    if (lista.isEmpty()) {
                        binding.textListaVazia.visibility = View.VISIBLE
                    } else {
                        binding.textListaVazia.visibility = View.GONE
                        rotaAdapter.submitList(lista)
                    }
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, status.erro, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.statusExclusao.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    alertaCarregamento.exibir("Excluindo rota...")
                }
                is UIstatus.Sucesso -> {
                    alertaCarregamento.fechar()
                    Toast.makeText(context, "Rota removida", Toast.LENGTH_SHORT).show()
                    motorista?.let { viewModel.listarRotasMotorista(it.id) }
                }
                is UIstatus.Erro -> {
                    alertaCarregamento.fechar()
                    Toast.makeText(context, status.erro, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun exibirDialogoConfirmacao(rota: Rota) {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir Rota")
            .setMessage("Deseja realmente excluir a rota da OS ${rota.os}?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.excluirRota(rota.id)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alertaCarregamento.fechar()
        _binding = null
    }
}
