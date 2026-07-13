package com.douglas2990.d2990entregasv2.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.douglas2990.d2990entregasv2.R
import com.douglas2990.d2990entregasv2.databinding.FragmentEntregasDoDiaBinding
import com.douglas2990.d2990entregasv2.model.Rota
import com.douglas2990.d2990entregasv2.presentation.ui.adapter.RotaAdapter
import com.douglas2990.d2990entregasv2.presentation.viewmodel.RotaViewModel
import com.example.core.AlertaCarregamento
import com.example.core.UIstatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntregasdoDiaFragment : Fragment() {

    private var _binding: FragmentEntregasDoDiaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RotaViewModel by viewModels()
    private lateinit var rotaAdapter: RotaAdapter
    private val alertaCarregamento by lazy { AlertaCarregamento(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntregasDoDiaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        viewModel.listarTodasAsRotas()
    }

    private fun setupRecyclerView() {
        // 🚀 Atualizado para encaixar perfeitamente com o construtor de 3 parâmetros do seu novo RotaAdapter
        rotaAdapter = RotaAdapter(
            onItemClick = { rota ->
                if (rota.status == "CONCLUIDA") {
                    Toast.makeText(requireContext(), "Entrega Concluída!", Toast.LENGTH_SHORT).show()
                }
            },
            onEditarClick = { rota ->
                abrirTelaEdicao(rota)
            },
            onDeletarClick = { rota ->
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
                        rotaAdapter.submitList(emptyList()) // Garante que a lista visual seja limpa
                    } else {
                        binding.textListaVazia.visibility = View.GONE
                        rotaAdapter.submitList(lista)
                    }
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), status.erro, Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(requireContext(), "Rota removida", Toast.LENGTH_SHORT).show()
                    viewModel.listarTodasAsRotas() // Atualiza a lista geral na hora
                }
                is UIstatus.Erro -> {
                    alertaCarregamento.fechar()
                    Toast.makeText(requireContext(), status.erro, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 🚀 Método adicionado para abrir o FragmentEditarRota enviando os dados da OS clicada
    private fun abrirTelaEdicao(rota: Rota) {
        val bundle = Bundle().apply {
            putParcelable("rotaParaEditar", rota)
        }
        findNavController().navigate(
            R.id.action_rotasDoMotoristaFragment_to_fragmentEditarRota,
            bundle
        )
    }

    private fun exibirDialogoConfirmacao(rota: Rota) {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir Rota")
            .setMessage("Deseja realmente excluir a rota da OS ${rota.os}? Ela sumirá do aplicativo do motorista associado.")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.excluirRota(rota.id)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alertaCarregamento.fechar() // Garante o fechamento seguro do dialog caso mude de tela no loading
        _binding = null
    }
}