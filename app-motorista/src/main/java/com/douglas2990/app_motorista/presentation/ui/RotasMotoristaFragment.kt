package com.douglas2990.app_motorista.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.douglas2990.app_motorista.R
import com.douglas2990.app_motorista.data.repository.model.AgendaDia
import com.douglas2990.app_motorista.databinding.FragmentRotasMotoristaBinding
import com.example.core.model.Rota
import com.douglas2990.app_motorista.presentation.ui.adapter.RotaAdapter
import com.douglas2990.app_motorista.presentation.viewmodel.RotasMotoristaViewModel
import com.example.core.UIstatus
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.core.model.Motorista
import com.example.core.util.ShareHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class RotasMotoristaFragment : Fragment() {

    private var _binding: FragmentRotasMotoristaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RotasMotoristaViewModel by viewModels()

    //val dataSelecionada = arguments?.getString("data") ?: ""
    private var nomeEmpresaAtual: String = "D2990 Entregas"

    private var telefoneDoGestor: String? = null
    private val shareHelper by lazy { ShareHelper(requireContext()) }
    private lateinit var rotaAdapter: RotaAdapter

    private var dataAtualSelecionada: String = ""

    private var dialogoExibidoNestaSessao = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRotasMotoristaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        setupRecyclerView()
        setupObservers()
        setupResultListener()
        setupClickListeners()

        // Pegamos o objeto AgendaDia que você enviou da Agenda
        val agendaDia = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13 ou superior
            arguments?.getParcelable("agenda_dia", AgendaDia::class.java)
        } else {
            // Para versões anteriores (o cast manual ainda é necessário aqui)
            @Suppress("DEPRECATION")
            arguments?.getParcelable<AgendaDia>("agenda_dia")
        }

        dataAtualSelecionada = agendaDia?.data ?: ""


        //this.dataAtualSelecionada = dataExtraida // Salva na variável global da classe

        Log.d("DEBUG_ROTAS", "Data extraída: $dataAtualSelecionada")

        if (dataAtualSelecionada.isNotEmpty()) {
            viewModel.observarMinhasRotas(dataAtualSelecionada)
            viewModel.carregarDadosMotorista()
        }else {
            Log.e("DEBUG_ROTAS", "ERRO: dataAtualSelecionada vazia!")
            binding.textListaVazia.visibility = View.VISIBLE
        }


       /* val dataParaFiltrar = agendaDia?.data ?: ""
        Log.d("DEBUG_ROTAS", "Data extraída: $dataParaFiltrar")

        // Dispara a busca das rotas vinculadas ao UID do motorista logado
        // Agora sim, com a data preenchida, chamamos o ViewModel
        if (dataParaFiltrar.isNotEmpty()) {
            viewModel.observarMinhasRotas(dataParaFiltrar)
            viewModel.carregarDadosMotorista()
        } else {
            Log.e("DEBUG_ROTAS", "ERRO: dataParaFiltrar continua vazia!")
            binding.textListaVazia.visibility = View.VISIBLE
        }*/
    }

    private fun setupResultListener() {
        parentFragmentManager.setFragmentResultListener("chave_finalizacao", viewLifecycleOwner) { _, bundle ->
            if (bundle.getBoolean("ultima_finalizada")) {
                binding.root.postDelayed({
                    if (isAdded) {
                        val statusAtual = viewModel.rotas.value
                        if (statusAtual is UIstatus.Sucesso) {
                            mostrarDialogoRelatorioFinal(statusAtual.dados ?: emptyList())
                        }
                    }
                }, 600)
            }
        }
    }

    private fun setupRecyclerView() {
        rotaAdapter = RotaAdapter(
            onItemClick = { rota ->

                val qtdPendentes = rotaAdapter.currentList.count { it.status == "PENDENTE" }
                //val ehAUltima = listaPendente.size == 1

                irParaTelaEntrega(rota, qtdPendentes == 1)
            },
            onItemLongClick = { /* O motorista não exclui rotas, apenas visualiza */ }
        )

        binding.rvRotasMotorista.apply {
            adapter = rotaAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {

        viewModel.nomeEmpresa.observe(viewLifecycleOwner) { empresa ->
            if (!empresa.isNullOrBlank()) {
                nomeEmpresaAtual = empresa
            }
        }

        viewModel.telefoneAdmin.observe(viewLifecycleOwner) { telefone ->
            if (telefone != null) {
                this.telefoneDoGestor = telefone
                Log.d("DEBUG_ROTAS", "Telefone do Admin carregado: $telefone")
            }
        }

        viewModel.arquivoPdfGerado.observe(viewLifecycleOwner) { arquivo ->
            binding.btnEnviarRelatorio.isEnabled = true
            binding.progressBar.visibility = View.GONE

            if (arquivo != null) {
                // Usamos o telefone que foi salvo anteriormente
                val fone = telefoneDoGestor ?: ""

                if (fone.isNotEmpty()) {
                    // Agora passa o arquivo e o telefone para o ShareHelper
                    ShareHelper(requireContext()).compartilharPdf(arquivo, fone)
                } else {
                    // Caso o telefone ainda não tenha carregado (por lentidão do Firebase)
                    // você pode chamar o seletor padrão ou avisar o usuário
                    Toast.makeText(context, "Contato do Admin ainda não carregado", Toast.LENGTH_SHORT).show()
                    ShareHelper(requireContext()).compartilharPdf(arquivo, "")
                }
            }
        }

        viewModel.statusArquivamento.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> binding.progressBar.visibility = View.VISIBLE
                is UIstatus.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Dia enviado para o histórico!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack() // Volta para a Agenda
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, status.erro, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.rotas.observe(viewLifecycleOwner) { status ->
            if (status !is UIstatus.Carregando) {
                binding.progressBar.visibility = View.GONE
            }

            when (status) {
                is UIstatus.Carregando -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.textListaVazia.visibility = View.GONE
                }
                is UIstatus.Sucesso -> {
                    val listaBruta = status.dados ?: emptyList()
                    val isHistorico = arguments?.getBoolean("is_historico") ?: false

                    val listaFiltrada = if (isHistorico) {
                        listaBruta.filter { it.status != "PENDENTE" }
                    } else {
                        listaBruta
                    }

                    rotaAdapter.submitList(listaFiltrada)

                    // Cálculo se tudo foi feito
                    val todasConcluidas = listaFiltrada.isNotEmpty() && listaFiltrada.all { it.status != "PENDENTE" }

                    // Controle de Diálogo Automático
                    if (!isHistorico && todasConcluidas && !dialogoExibidoNestaSessao) {
                        binding.root.postDelayed({
                            if (isAdded) {
                                mostrarDialogoRelatorioFinal(listaFiltrada)
                                dialogoExibidoNestaSessao = true
                            }
                        }, 500)
                    }

                    // Atualiza apenas Visibilidade
                    if (isHistorico) {
                        binding.btnEnviarRelatorio.visibility = if (listaFiltrada.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.btnArquivar.visibility = View.GONE
                    } else {
                        binding.btnEnviarRelatorio.visibility = if (todasConcluidas) View.VISIBLE else View.GONE
                        binding.btnArquivar.visibility = if (todasConcluidas) View.VISIBLE else View.GONE
                    }

                    binding.textListaVazia.visibility = if (listaFiltrada.isEmpty()) View.VISIBLE else View.GONE

                    if (listaFiltrada.isNotEmpty()) {
                        viewModel.buscarTelefoneAdmin(listaFiltrada[0].idGestor)
                    }
                }
                is UIstatus.Erro -> {
                    Toast.makeText(requireContext(), status.erro, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupClickListeners() {

        binding.btnVoltar.setOnClickListener { findNavController().popBackStack() }

        binding.btnArquivar.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            if (uid.isNotEmpty() && dataAtualSelecionada.isNotEmpty()) {
                viewModel.arquivarDia(uid, dataAtualSelecionada)
            }
        }

        binding.btnEnviarRelatorio.setOnClickListener {
            val statusAtual = viewModel.rotas.value
            if (statusAtual is UIstatus.Sucesso) {
                val listaParaRelatorio = statusAtual.dados?.filter { it.status != "PENDENTE" } ?: emptyList()
                if (listaParaRelatorio.isNotEmpty()) {
                    binding.btnEnviarRelatorio.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.gerarRelatorio(requireContext(), listaParaRelatorio)
                }
            }
        }

        //binding.btnVoltar.setOnClickListener { findNavController().popBackStack() }
    }

    private fun irParaTelaEntrega(rota: Rota, ehAUltima: Boolean) {
        // Usando Bundle para passar a rota selecionada para o DetalhesEntregaFragment
        val bundle = Bundle().apply {
            putParcelable("rota", rota)
            putBoolean("sou_a_ultima", ehAUltima)
        }
        findNavController().navigate(
            R.id.action_rotasMotoristaFragment_to_detalhesEntregaFragment,
            bundle
        )
    }



    private fun mostrarDialogoRelatorioFinal(listaParaRelatorio: List<Rota>) {
        // Primeiro Diálogo: Oferece o envio imediato
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Todas as entregas concluídas!")
            .setMessage("Deseja enviar o relatório de comprovantes agora para o WhatsApp?")
            .setPositiveButton("Sim, enviar") { _, _ ->
                // Dispara a lógica de PDF que você já tem
                binding.progressBar.visibility = View.VISIBLE
                viewModel.gerarRelatorio(requireContext(), listaParaRelatorio)
            }
            .setNegativeButton("Agora não") { _, _ ->
                // Segundo Diálogo: Informa onde encontrar depois
                mostrarAvisoHistorico()
            }
            .setCancelable(false) // Obriga o motorista a escolher uma opção
            .show()
    }

    private fun mostrarAvisoHistorico() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Relatório Disponível")
            .setMessage("Sem problemas! Caso queira enviar o relatório mais tarde, você pode acessá-lo no seu Histórico.")
            .setPositiveButton("Entendido") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}