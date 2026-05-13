package com.douglas2990.d2990entregasv2.presentation.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.douglas2990.d2990entregasv2.R
import com.douglas2990.d2990entregasv2.databinding.FragmentDatasRotasAdminBinding
import com.douglas2990.d2990entregasv2.model.Motorista
import com.douglas2990.d2990entregasv2.presentation.adapter.DataAgendaAdapter
import com.douglas2990.d2990entregasv2.presentation.viewmodel.RotaViewModel
import com.example.core.UIstatus
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DatasRotasAdminFragment : Fragment() {

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

        setupMenu()

        setupRecyclerView()
        setupObservers()

        motorista?.let {
            binding.textTituloDatas.text = "Agenda de ${it.nome}"
            // ATENÇÃO: Mudamos a chamada para a nova função com Status (Semáforo)
            //viewModel.listarDatasComStatusAdmin(it.id)
            viewModel.listarAgendaAtiva(it.id)
        }
    }


        private fun setupMenu() {
            requireActivity().addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_agenda_admin, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.item_historico -> {
                            irParaHistorico()
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }

    private fun irParaHistorico() {
        val bundle = Bundle().apply {
            putParcelable("motorista", motorista)
        }
        findNavController().navigate(R.id.action_datasRotasAdmin_to_datasArquivadas, bundle)
    }


    private fun setupRecyclerView() {
        dataAdapter = DataAgendaAdapter(
            onDataClick = { dataLong ->
                // Chamamos a função de navegação aqui
                irParaRotas(dataLong)
            },
            onArquivarClick = { dataLong ->
                exibirDialogConfirmacaoArquivar(dataLong)
            }
        )

        binding.rvDatas.adapter = dataAdapter
        binding.rvDatas.layoutManager = LinearLayoutManager(requireContext())
    }

    // FUNÇÃO DE NAVEGAÇÃO ORGANIZADA
    private fun irParaRotas(dataLong: Long) {
        val bundle = Bundle().apply {
            putParcelable("motorista", motorista)
            putLong("data_selecionada", dataLong)
        }
        findNavController().navigate(R.id.action_datasRotasAdmin_to_rotasDoMotorista, bundle)
    }

    private fun setupObservers() {
        // IMPORTANTE: Agora observamos 'datasStatus' em vez de 'datasComRotas'
        /*viewModel.datasStatus.observe(viewLifecycleOwner) { status ->
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
        }*/
        viewModel.agendaAtiva.observe(viewLifecycleOwner) { status ->
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

// No seu DatasRotasAdminFragment.kt dentro do setupObservers()

        viewModel.statusArquivamento.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Sucesso -> {
                    Toast.makeText(context, "Dia movido para o arquivo!", Toast.LENGTH_SHORT).show()


                    findNavController().navigate(
                        R.id.action_datasRotasAdmin_to_datasArquivadas,
                        Bundle().apply { putParcelable("motorista", motorista) },
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.datasRotasAdminFragment, true) // Nome do fragmento no nav_graph
                            .build()
                    )
                }
                is UIstatus.Erro -> {
                    Toast.makeText(context, "Erro ao arquivar: ${status.erro}", Toast.LENGTH_LONG).show()
                }
                is UIstatus.Carregando -> {
                    // Pode mostrar um loading se quiser
                }
            }
        }

    }

    private fun exibirDialogConfirmacaoArquivar(data: Long) {
        AlertDialog.Builder(requireContext())
            .setTitle("Arquivar entregas")
            .setMessage("Deseja mover as rotas deste dia para o histórico arquivado?")
            .setPositiveButton("Sim, arquivar") { _, _ ->
                motorista?.id?.let { idMotorista ->
                    viewModel.arquivarDia(idMotorista, data)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}