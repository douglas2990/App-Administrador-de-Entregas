package com.douglas2990.app_motorista.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.douglas2990.app_motorista.R
import com.douglas2990.app_motorista.databinding.FragmentAgendaBinding
import com.douglas2990.app_motorista.presentation.ui.adapter.AgendaAdapter
import com.douglas2990.app_motorista.presentation.viewmodel.AgendaViewModel
import com.example.core.UIstatus
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AgendaFragment : Fragment() {

    private var _binding: FragmentAgendaBinding? = null
    private val binding get() = _binding!!

    // 1. INJEÇÃO DO FIREBASE AUTH (Hilt cuida disso para você)
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: AgendaViewModel by viewModels()
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


        configurarRecyclerView()
        observarDados()

        // 2. PEGANDO O UID REAL E DISPARANDO O CARREGAMENTO
        val uidAtual = firebaseAuth.currentUser?.uid
        if (uidAtual != null) {
            Log.d("DEBUG_AGENDA", "Iniciando busca para o UID: $uidAtual")
            viewModel.carregarAgenda(idMotorista = uidAtual)
        } else {
            Toast.makeText(context, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
        }

        binding.btnVoltar.setOnClickListener {
            findNavController().popBackStack()
        }

        acesso_menu_agenda()

        binding.swipeRefreshAgenda.setOnRefreshListener {
            val uidAtual = firebaseAuth.currentUser?.uid
            if (uidAtual != null) {
                viewModel.carregarAgenda(idMotorista = uidAtual)
            } else {
                binding.swipeRefreshAgenda.isRefreshing = false
            }
        }
    }

    private fun configurarRecyclerView() {
        // Certifique-se de que o clique está passando o objeto correto
        agendaAdapter = AgendaAdapter { agendaDia ->
            val bundle = Bundle().apply {
                // Verifique se AgendaDia implementa Parcelable no seu modelo!
                putParcelable("agenda_dia", agendaDia)
            }
            findNavController().navigate(
                R.id.action_agendaFragment_to_rotasDoMotoristaFragment, // Use o ID correto do seu nav_graph
                bundle
            )
        }

        binding.rvAgenda.apply {
            adapter = agendaAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun observarDados() {
        viewModel.statusAgenda.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    if (!binding.swipeRefreshAgenda.isRefreshing) {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    binding.textListaVazia.visibility = View.GONE
                }
                is UIstatus.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshAgenda.isRefreshing = false
                    if (status.dados.isEmpty()) {
                        binding.textListaVazia.visibility = View.VISIBLE
                        agendaAdapter.adicionarLista(emptyList()) // Limpa se estiver vazio
                    } else {
                        binding.textListaVazia.visibility = View.GONE
                        agendaAdapter.adicionarLista(status.dados)
                    }
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshAgenda.isRefreshing = false
                    Toast.makeText(context, "Erro: ${status.erro}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun acesso_menu_agenda(){
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_agenda, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.historicoFragment -> {
                        // Navega automaticamente se o ID do menu for igual ao ID do NavGraph
                        findNavController().navigate(R.id.historicoFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}