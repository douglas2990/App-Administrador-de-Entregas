package com.douglas2990.app_motorista.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.douglas2990.app_motorista.R
import com.douglas2990.app_motorista.databinding.FragmentListarObservacoesBinding
import com.douglas2990.app_motorista.presentation.ui.adapter.ObservacoesAdapter
import com.douglas2990.app_motorista.presentation.viewmodel.ObservacoesViewModel
import com.example.core.UIstatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListarObservacoesFragment : Fragment() {

    private var _binding: FragmentListarObservacoesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ObservacoesViewModel by viewModels()
    private val adapter = ObservacoesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListarObservacoesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observarDados()

        viewModel.listarObservacoes()

        binding.btnVoltar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fabAdicionar.setOnClickListener {
            findNavController().navigate(R.id.action_listarObservacoesFragment_to_cadastrarObservacaoFragment)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.listarObservacoes()
        }
    }

    private fun setupRecyclerView() {
        binding.rvObservacoes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvObservacoes.adapter = adapter
    }

    private fun observarDados() {
        viewModel.observacoes.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    if (!binding.swipeRefresh.isRefreshing) {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
                is UIstatus.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    adapter.adicionarLista(status.dados)
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(requireContext(), status.erro, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
