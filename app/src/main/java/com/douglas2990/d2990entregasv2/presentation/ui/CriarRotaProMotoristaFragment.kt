package com.douglas2990.d2990entregasv2.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.douglas2990.d2990entregasv2.databinding.FragmentCriarRotaProMotoristaBinding
import com.douglas2990.d2990entregasv2.model.Motorista
import com.douglas2990.d2990entregasv2.model.Rota
import com.douglas2990.d2990entregasv2.presentation.viewmodel.ListarMotoristasViewModel
import com.douglas2990.d2990entregasv2.presentation.viewmodel.RotaViewModel
import com.example.core.UIstatus
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CriarRotaProMotoristaFragment : Fragment() {

    private var _binding: FragmentCriarRotaProMotoristaBinding? = null
    private val binding get() = _binding!!

    private val rotaViewModel: RotaViewModel by viewModels()
    private val motoristasViewModel: ListarMotoristasViewModel by viewModels()

    private var listaMotoristas: List<Motorista> = emptyList()
    private var motoristaPreSelecionado: Motorista? = null
    private var dataPrevistaSelecionada: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCriarRotaProMotoristaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            motoristaPreSelecionado = BundleCompat.getParcelable(
                args,
                "detalhe_motorista",
                Motorista::class.java
            )
        }

        setupObservers()
        setupListeners()
        motoristasViewModel.carregarMotoristas()
    }

    private fun setupObservers() {
        // Observa a lista de motoristas para preencher o AutoCompleteTextView (Dropdown)
        motoristasViewModel.motoristas.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is UIstatus.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    listaMotoristas = status.dados ?: emptyList()

                    val nomes = listaMotoristas.map { it.nome }

                    // Ajustado para usar o layout do dropdown adequado para AutoCompleteTextView
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        nomes
                    )
                    binding.spinnerMotoristas.setAdapter(adapter)

                    // Se houver um motorista pré-selecionado, define o texto direto
                    motoristaPreSelecionado?.let { pre ->
                        val index = listaMotoristas.indexOfFirst { it.id == pre.id }
                        if (index != -1) {
                            // setText define o valor visual. O "false" evita que ele filtre a lista ao abrir
                            binding.spinnerMotoristas.setText(pre.nome, false)
                        }
                    }
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, status.erro, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observa o status do salvamento da rota
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
        // Listener para abrir o Calendário (DatePicker)
        binding.editDataPrevista.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione a data da entrega")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                dataPrevistaSelecionada = selection

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                    timeZone = java.util.TimeZone.getTimeZone("UTC")
                }

                val dateString = sdf.format(Date(selection))
                binding.editDataPrevista.setText(dateString)
                binding.inputLayoutDataPrevista.error = null
            }

            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        // Listener para o botão Salvar
        binding.btnSalvarRota.setOnClickListener {
            val os = binding.editOS.text.toString()
            val empresa = binding.editEmpresaDestino.text.toString()
            val endereco = binding.editEndereco.text.toString()

            // Captura o texto que está selecionado atualmente no dropdown
            val nomeSelecionado = binding.spinnerMotoristas.text.toString()

            // Procura o objeto Motorista correspondente ao nome que está escrito no campo
            val motorista = listaMotoristas.find { it.nome == nomeSelecionado }

            // VALIDAÇÃO COMPLETA: Incluindo Data e Motorista
            if (os.isEmpty() || empresa.isEmpty() || endereco.isEmpty() || dataPrevistaSelecionada == null || motorista == null) {

                if (dataPrevistaSelecionada == null) {
                    binding.inputLayoutDataPrevista.error = "A data prevista é obrigatória!"
                }

                if (motorista == null) {
                    binding.inputLayoutSpinner.error = "Selecione um motorista válido!"
                }

                Toast.makeText(context, "Atenção: Preencha todos os campos e a data!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Criação do objeto Rota com todos os campos necessários
            val novaRota = Rota(
                os = os,
                nomeEmpresaDestino = empresa,
                endereco = endereco,
                dataPrevista = dataPrevistaSelecionada!!,
                idMotorista = motorista.id,
                nomeMotorista = motorista.nome,
                status = "PENDENTE"
            )

            rotaViewModel.salvarRota(novaRota)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}