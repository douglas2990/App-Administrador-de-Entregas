package com.douglas2990.d2990entregasv2.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.douglas2990.d2990entregasv2.R
import com.douglas2990.d2990entregasv2.databinding.FragmentDatasRotasAdminBinding
import com.douglas2990.d2990entregasv2.databinding.FragmentRotasArquivadasBinding
import com.douglas2990.d2990entregasv2.model.Motorista
import com.douglas2990.d2990entregasv2.presentation.adapter.DataAgendaAdapter
import com.douglas2990.d2990entregasv2.presentation.viewmodel.RotaViewModel
import com.example.core.UIstatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DatasArquivadasFragment : Fragment() {

    private var _binding: FragmentDatasRotasAdminBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RotaViewModel by viewModels()
    private lateinit var dataAdapter: DataAgendaAdapter
    private var motorista: Motorista? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            motorista = BundleCompat.getParcelable(it, "motorista", Motorista::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDatasRotasAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()

        motorista?.let {
            binding.textTituloDatas.text = "Histórico: ${it.nome}"
            viewModel.listarAgendaArquivada(it.id)
        }
    }

    private fun setupRecyclerView() {
        dataAdapter = DataAgendaAdapter(
            onDataClick = { dataLong ->
                val bundle = Bundle().apply {
                    putParcelable("motorista", motorista)
                    putLong("data_selecionada", dataLong)
                    putBoolean("is_arquivado", true) // Marca como histórico
                }
                findNavController().navigate(R.id.action_datasArquivadas_to_rotasDoMotorista, bundle)
            },
            onArquivarClick = { /* Vazio: Não arquivamos o que já está arquivado */ }
        )
        binding.rvDatas.adapter = dataAdapter
        binding.rvDatas.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObservers() {
        viewModel.agendaArquivada.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> binding.progressDatas.visibility = View.VISIBLE
                is UIstatus.Sucesso -> {
                    binding.progressDatas.visibility = View.GONE
                    dataAdapter.submitList(status.dados)
                }
                is UIstatus.Erro -> {
                    binding.progressDatas.visibility = View.GONE
                    Toast.makeText(context, status.erro, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}