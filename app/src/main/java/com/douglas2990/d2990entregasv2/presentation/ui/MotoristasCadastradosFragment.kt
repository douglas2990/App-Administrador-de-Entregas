package com.douglas2990.d2990entregasv2.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.douglas2990.d2990entregasv2.R
import com.douglas2990.d2990entregasv2.databinding.FragmentMotoristasCadastradosBinding
import com.douglas2990.d2990entregasv2.presentation.adapter.MotoristaAdapter
import com.douglas2990.d2990entregasv2.presentation.viewmodel.ListarMotoristasViewModel
import com.example.core.AlertaCarregamento
import com.example.core.UIstatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MotoristasCadastradosFragment : Fragment() {

    private var _binding: FragmentMotoristasCadastradosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListarMotoristasViewModel by viewModels()

    private lateinit var motoristaAdapter: MotoristaAdapter
    private val alertaCarregamento by lazy { AlertaCarregamento(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMotoristasCadastradosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarRecyclerView()
        configurarObservadores()
        configurarCliques()

        // Dispara a busca no Firebase (Onde o filtro idGestor acontece)
        viewModel.carregarMotoristas()
    }

    private fun configurarRecyclerView() {
        motoristaAdapter = MotoristaAdapter(
            onNovaRotaClick = { motorista ->
                val bundle = Bundle().apply {
                    putParcelable("detalhe_motorista", motorista)// Use um nome bem único
                }
                findNavController().navigate(
                    R.id.action_motoristascadastradosFragment_to_criarRotaProMotoristaFragment,
                    bundle
                )
            },
            onVerRotasClick = { motorista ->
                val bundle = Bundle().apply {
                    putParcelable("motorista", motorista)
                }
                findNavController().navigate(
                    R.id.action_motoristascadastradosFragment_to_rotasDoMotoristaFragment,
                    bundle
                )
            }
        )

        binding.rvMotoristas.apply {
            adapter = motoristaAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun configurarObservadores() {
        viewModel.motoristas.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    alertaCarregamento.exibir("Buscando motoristas...")
                }
                is UIstatus.Sucesso -> {
                    alertaCarregamento.fechar()
                    val lista = status.dados

                    if (lista.isEmpty()) {
                        binding.textListaVazia.visibility = View.VISIBLE
                    } else {
                        binding.textListaVazia.visibility = View.GONE
                        motoristaAdapter.submitList(lista)
                    }
                }
                is UIstatus.Erro -> {
                    alertaCarregamento.fechar()
                    Toast.makeText(requireContext(), status.erro, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun configurarCliques() {
        // Botão para ver todas as rotas de todos os motoristas
        binding.btnVerTodasRotas.setOnClickListener {
            findNavController().navigate(R.id.action_motoristascadastradosFragment_to_entregasdoDiaFragment)
        }

        // Botão flutuante para cadastrar novo motorista
        binding.fabAddMotorista.setOnClickListener {
            findNavController().navigate(R.id.action_motoristascadastradosFragment_to_CadastrarMotoristaFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alertaCarregamento.fechar() // Evita WindowLeaked
        _binding = null
    }
}