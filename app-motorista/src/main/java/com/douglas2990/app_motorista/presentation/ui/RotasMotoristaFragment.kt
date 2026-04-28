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


        // Pegamos o objeto AgendaDia que você enviou da Agenda
        val agendaDia = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13 ou superior
            arguments?.getParcelable("agenda_dia", AgendaDia::class.java)
        } else {
            // Para versões anteriores (o cast manual ainda é necessário aqui)
            @Suppress("DEPRECATION")
            arguments?.getParcelable<AgendaDia>("agenda_dia")
        }

        val dataExtraida = agendaDia?.data ?: ""


        this.dataAtualSelecionada = dataExtraida // Salva na variável global da classe

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

    private fun setupRecyclerView() {
        rotaAdapter = RotaAdapter(
            onItemClick = { rota ->
                irParaTelaEntrega(rota)
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

            // REGRA DE OURO: Se não está mais carregando, para as animações
            if (status !is UIstatus.Carregando) {
                binding.progressBar.visibility = View.GONE
                //binding.swipeRefresh.isRefreshing = false
            }

            when (status) {
                is UIstatus.Carregando -> {
                    // Só mostra o progress central se não for um "puxar para atualizar"
                   // if (!binding.swipeRefresh.isRefreshing) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.textListaVazia.visibility = View.GONE
                    //}
                }
                is UIstatus.Sucesso -> {
                    //val lista = status.dados ?: emptyList()
                    val listaBruta = status.dados ?: emptyList()

                    val isHistorico = arguments?.getBoolean("is_historico") ?: false

                    val listaFiltrada = if (isHistorico) {
                        // Se for histórico, mostra o que já foi finalizado
                        listaBruta.filter { it.status != "PENDENTE" }
                    } else {
                        // Se for agenda normal, mostra apenas o que ainda é pendente
                        listaBruta.filter { it.status == "PENDENTE" }
                    }

                    rotaAdapter.submitList(listaFiltrada)

                    val todasConcluidas = listaFiltrada.isNotEmpty() && listaFiltrada.all { it.status != "PENDENTE" }

                    if (listaFiltrada.isNotEmpty()) {
                        viewModel.buscarTelefoneAdmin(listaFiltrada[0].idGestor)
                    }

                    if (isHistorico) {
                        binding.btnEnviarRelatorio.visibility = if (listaFiltrada.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.btnArquivar.visibility = View.GONE
                    } else {
                        binding.btnEnviarRelatorio.visibility = if (todasConcluidas) View.VISIBLE else View.GONE
                        binding.btnArquivar.visibility = if (todasConcluidas) View.VISIBLE else View.GONE
                    }

                    binding.btnArquivar.setOnClickListener {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        // dataParaFiltrar é a variável que você já extraiu do Bundle no onViewCreated
                        viewModel.arquivarDia(uid, dataAtualSelecionada)
                    }

                    binding.textListaVazia.visibility = if (listaFiltrada.isEmpty()) View.VISIBLE else View.GONE


                    binding.btnEnviarRelatorio.setOnClickListener {
                        // 1. Pegamos a lista do status atual
                        val statusAtual = viewModel.rotas.value

                        if (statusAtual is UIstatus.Sucesso) {
                            val listaCompleta = statusAtual.dados ?: emptyList()

                            // 2. Opcional: Filtramos para enviar apenas o que não está pendente
                            val listaParaRelatorio = listaCompleta.filter { it.status != "PENDENTE" }

                            if (listaParaRelatorio.isNotEmpty()) {
                                // 3. Feedback visual: desativa o botão e mostra o progress
                                binding.btnEnviarRelatorio.isEnabled = false
                                binding.progressBar.visibility = View.VISIBLE

                                // 4. Ordem para a ViewModel
                                viewModel.gerarRelatorio(requireContext(), listaParaRelatorio)
                            } else {
                                Toast.makeText(requireContext(), "Não há entregas finalizadas para o relatório", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    binding.btnArquivar.setOnClickListener {
                        val uidMotorista = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        if (uidMotorista.isNotEmpty() && dataAtualSelecionada.isNotEmpty()) {
                            viewModel.arquivarDia(uidMotorista, dataAtualSelecionada)
                        }
                    }



                    //btnEnviarRelatorioPDF(lista, nomeEmpresaAtual)

                    // Gerencia o texto de lista vazia
                    binding.textListaVazia
                        .visibility = if (listaFiltrada.isEmpty()) View.VISIBLE else View.GONE
                }
                is UIstatus.Erro -> {
                    // Usa a variável 'mensagem' que definimos no :core
                    Toast.makeText(requireContext(), status.erro, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irParaTelaEntrega(rota: Rota) {
        // Usando Bundle para passar a rota selecionada para o DetalhesEntregaFragment
        val bundle = Bundle().apply {
            putParcelable("rota", rota)
        }
        findNavController().navigate(
            R.id.action_rotasMotoristaFragment_to_detalhesEntregaFragment,
            bundle
        )
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}