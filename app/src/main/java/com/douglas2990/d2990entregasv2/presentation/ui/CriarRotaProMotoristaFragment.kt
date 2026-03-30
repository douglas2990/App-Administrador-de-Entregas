package com.douglas2990.d2990entregasv2.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.douglas2990.d2990entregasv2.databinding.FragmentCriarRotaProMotoristaBinding
import com.douglas2990.d2990entregasv2.model.Motorista
import com.douglas2990.d2990entregasv2.model.Rota
import com.douglas2990.d2990entregasv2.presentation.viewmodel.ListarMotoristasViewModel
import com.douglas2990.d2990entregasv2.presentation.viewmodel.RotaViewModel
import com.example.core.UIstatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CriarRotaProMotoristaFragment : Fragment() {

    private var _binding: FragmentCriarRotaProMotoristaBinding? = null
    private val binding get() = _binding!!

    private val rotaViewModel: RotaViewModel by viewModels()
    private val motoristasViewModel: ListarMotoristasViewModel by viewModels()

    private var listaMotoristas: List<Motorista> = emptyList()

    private var motoristaPreSelecionado: Motorista? = null // Adicione esta linha

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCriarRotaProMotoristaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        motoristaPreSelecionado = arguments?.getParcelable("motorista")

        setupObservers()
        setupListeners()
        motoristasViewModel.carregarMotoristas()
    }

    private fun setupObservers() {
        motoristasViewModel.motoristas.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is UIstatus.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    listaMotoristas = status.dados ?: emptyList()
                    val nomes = listaMotoristas.map { it.nome }
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nomes)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerMotoristas.adapter = adapter

                    motoristaPreSelecionado?.let { pre ->
                        // Procura a posição do motorista na lista carregada pelo ID
                        val index = listaMotoristas.indexOfFirst { it.id == pre.id }
                        if (index != -1) {
                            binding.spinnerMotoristas.setSelection(index)
                        }
                    }

                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, status.erro, Toast.LENGTH_SHORT).show()
                }
            }
        }

        rotaViewModel.statusSalvar.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSalvarRota.isEnabled = false
                }
                is UIstatus.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Rota criada com sucesso!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSalvarRota.isEnabled = true
                    Toast.makeText(context, status.erro, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnSalvarRota.setOnClickListener {
            val os = binding.editOS.text.toString()
            val empresaDestino = binding.editEmpresaDestino.text.toString()
            val endereco = binding.editEndereco.text.toString()
            val motoristaSelecionadoPos = binding.spinnerMotoristas.selectedItemPosition

            if (os.isNotEmpty() && empresaDestino.isNotEmpty() && endereco.isNotEmpty() && motoristaSelecionadoPos != -1) {
                val motorista = listaMotoristas[motoristaSelecionadoPos]

                val novaRota = Rota(
                    os = os,
                    nomeEmpresaDestino = empresaDestino,
                    endereco = endereco,
                    idMotorista = motorista.id,
                    nomeMotorista = motorista.nome
                )

                rotaViewModel.salvarRota(novaRota)
            } else {
                Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
