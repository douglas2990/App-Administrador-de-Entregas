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
import com.douglas2990.app_motorista.databinding.FragmentAgendaBinding
import com.douglas2990.app_motorista.presentation.ui.adapter.AgendaAdapter
import com.douglas2990.app_motorista.presentation.viewmodel.HistoricoViewModel
import com.example.core.UIstatus
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoricoFragment : Fragment() {

    private var _binding: FragmentAgendaBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: HistoricoViewModel by viewModels()
    private lateinit var agendaAdapter: AgendaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Customização da UI para o modo Histórico
        binding.textTituloAgenda.text = "Meu Histórico"

        configurarRecyclerView()
        setupObservers()
        setupListeners()

        // Dispara o carregamento inicial
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            viewModel.carregarHistoricoAgrupado(uid)
        } else {
            Toast.makeText(context, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarRecyclerView() {
        agendaAdapter = AgendaAdapter { agendaDia ->
            val bundle = Bundle().apply {
                putParcelable("agenda_dia", agendaDia)
                putBoolean("is_historico", true) // Crucial para o filtro no próximo fragmento
            }
            findNavController().navigate(
                R.id.action_historicoFragment_to_rotasMotoristaFragment,
                bundle
            )
        }

        binding.rvAgenda.apply {
            adapter = agendaAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        viewModel.statusHistoricoAgenda.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.textListaVazia.visibility = View.GONE
                }
                is UIstatus.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    if (status.dados.isEmpty()) {
                        binding.textListaVazia.text = "Nenhum histórico encontrado"
                        binding.textListaVazia.visibility = View.VISIBLE
                        agendaAdapter.adicionarLista(emptyList())
                    } else {
                        binding.textListaVazia.visibility = View.GONE
                        agendaAdapter.adicionarLista(status.dados)
                    }
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Erro: ${status.erro}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnVoltar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}