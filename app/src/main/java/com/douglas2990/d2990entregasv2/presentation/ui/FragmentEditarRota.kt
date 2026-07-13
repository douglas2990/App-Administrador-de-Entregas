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
import com.douglas2990.d2990entregasv2.databinding.FragmentEditarRotaBinding
import com.douglas2990.d2990entregasv2.model.Rota
import com.douglas2990.d2990entregasv2.presentation.viewmodel.RotaViewModel
import com.example.core.AlertaCarregamento
import com.example.core.UIstatus
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class FragmentEditarRota : Fragment() {

    private var _binding: FragmentEditarRotaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RotaViewModel by viewModels()
    private val alertaCarregamento by lazy { AlertaCarregamento(requireContext()) }

    private var rotaOriginal: Rota? = null
    private var dataSelecionadaTimestamp: Long? = null

    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Pega a rota enviada pelo fragment anterior
            rotaOriginal = BundleCompat.getParcelable(it, "rotaParaEditar", Rota::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarRotaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preencherCamposIniciais()
        setupListeners()
        setupObservers()
    }

    private fun preencherCamposIniciais() {
        rotaOriginal?.let { rota ->
            binding.editOs.setText(rota.os)
            binding.editEmpresaDestino.setText(rota.nomeEmpresaDestino)
            binding.editEndereco.setText(rota.endereco)
            binding.editObservacao.setText(rota.observacao ?: "")

            // Configura a data atual da rota
            rota.dataPrevista?.let { timestamp ->
                dataSelecionadaTimestamp = timestamp
                binding.textDataSelecionada.text = sdf.format(Date(timestamp))
            }
        } ?: run {
            Toast.makeText(context, "Erro ao carregar dados da rota", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun setupListeners() {
        // Botão para Alterar Data usando o padrão Material Design
        binding.btnSelecionarData.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione a Data Prevista")
                .setSelection(dataSelecionadaTimestamp ?: MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                dataSelecionadaTimestamp = selection
                binding.textDataSelecionada.text = sdf.format(Date(selection))
            }
            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        // Botão Cancelar
        binding.btnCancelar.setOnClickListener {
            findNavController().popBackStack()
        }

        // Botão Salvar
        binding.btnSalvar.setOnClickListener {
            coletarESalvarAlteracoes()
        }
    }

    private fun coletarESalvarAlteracoes() {
        val os = binding.editOs.text.toString().trim()
        val empresa = binding.editEmpresaDestino.text.toString().trim()
        val endereco = binding.editEndereco.text.toString().trim()
        val observacao = binding.editObservacao.text.toString().trim()

        rotaOriginal?.let { rota ->
            // Cria uma cópia da rota mantendo ID, motorista e gestor originais, alterando os dados modificados
            val rotaAtualizada = rota.copy(
                os = os,
                nomeEmpresaDestino = empresa,
                endereco = endereco,
                observacao = observacao.ifEmpty { null },
                dataPrevista = dataSelecionadaTimestamp
            )

            // Dispara para a sua ViewModel (validações acontecem na UseCase que revisamos!)
            viewModel.salvarRota(rotaAtualizada)
        }
    }

    private fun setupObservers() {
        viewModel.statusSalvar.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    alertaCarregamento.exibir("Atualizando rota...")
                }
                is UIstatus.Sucesso -> {
                    alertaCarregamento.fechar()
                    Toast.makeText(context, "Rota atualizada com sucesso!", Toast.LENGTH_SHORT).show()

                    // Volta para a tela de listagem automaticamente
                    findNavController().popBackStack()
                }
                is UIstatus.Erro -> {
                    alertaCarregamento.fechar()
                    Toast.makeText(context, status.erro, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alertaCarregamento.fechar()
        _binding = null
    }
}