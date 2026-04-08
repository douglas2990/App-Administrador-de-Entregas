package com.douglas2990.d2990entregasv2.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.douglas2990.d2990entregasv2.databinding.FragmentEmpresasCadastradasBinding
import com.douglas2990.d2990entregasv2.model.Empresa
import com.douglas2990.d2990entregasv2.presentation.adapter.EmpresaAdapter
import com.douglas2990.d2990entregasv2.presentation.viewmodel.CadastroEmpresaViewModel
import com.example.core.AlertaCarregamento
import com.example.core.UIstatus
import com.example.core.exibirMensagem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class EmpresasCadastradasFragment : Fragment() {

    private var _binding: FragmentEmpresasCadastradasBinding? = null

    private val cadastroEmpresaViewModel: CadastroEmpresaViewModel by viewModels()

    // Inicialização segura do Adapter
    private val empresaAdapter by lazy {
        EmpresaAdapter(
            clickEditar = { empresa -> editar(empresa) },
            clickExcluir = { empresa -> confirmarExclusao(empresa) }
        )
    }

    private fun confirmarExclusao(empresa: Empresa) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Atenção")
            .setMessage("Deseja realmente excluir a empresa ${empresa.nome}?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Sim, excluir") { _, _ ->
                // Chama a função remover que já existe no seu Repository
                cadastroEmpresaViewModel.remover(empresa.id) { status ->
                    when(status) {
                        is UIstatus.Sucesso -> {
                            Toast.makeText(context, "Excluído!", Toast.LENGTH_SHORT).show()
                            recuperarEmpresas() // Atualiza a lista na tela
                        }
                        is UIstatus.Erro -> Toast.makeText(context, status.erro, Toast.LENGTH_SHORT).show()
                        else -> {}
                    }
                }
            }
            .show()
    }

    private fun editar(empresa: Empresa) {

        val acao = EmpresasCadastradasFragmentDirections
            .actionEmpresasCadastradasFragmentToCadastrarEmpresaFragment(empresa)

        findNavController().navigate(acao)
    }

    private val alertaCarregamento by lazy {
        AlertaCarregamento(requireContext())
    }
    private var idEmpresa = ""

    private val empresas = emptyList<Empresa>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEmpresasCadastradasBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recuperarEmpresas()
        inicializar()
        //configurarRecyclerView2()
        //inicializarObservaveis2()
        //cadastroEmpresaViewModel.listar2()

    }
    private fun inicializar() {
        inicializarRecyclerViewEmpresas()

    }

    private fun inicializarRecyclerViewEmpresas() {
        with(binding){
            empresaAdapter.adicionarLista( empresas )
            rvEmpresas.adapter = empresaAdapter
            rvEmpresas.layoutManager = LinearLayoutManager(
                requireContext().applicationContext, RecyclerView.VERTICAL, false
            )
        }
    }

    // No onViewCreated ou inicializarObservaveis
    private fun configurarRecyclerView2() {
        with(binding.rvEmpresas) { // 'rvEmpresas' é o ID do RecyclerView no seu XML
            setHasFixedSize(true) // Melhora a performance
            layoutManager = LinearLayoutManager(context)
            adapter = empresaAdapter
        }
    }

    private fun inicializarObservaveis2() {
        cadastroEmpresaViewModel.uiStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Sucesso -> {
                    // Como sua UIstatus pode retornar vários tipos,
                    // verificamos se o dado é realmente uma lista de empresas
                    val lista = status.dados as? List<Empresa>
                    if (lista != null) {
                        empresaAdapter.adicionarLista(lista)
                    }
                }
                is UIstatus.Erro -> {
                    exibirMensagem(status.erro)
                }
                is UIstatus.Carregando -> {
                    // Opcional: mostrar um ProgressBar aqui
                }
            }
        }
    }

    private fun recuperarEmpresas() {
        cadastroEmpresaViewModel.listar { uiStatus ->
            when( uiStatus ){
                is UIstatus.Erro -> {
                    alertaCarregamento.fechar()
                    exibirMensagem( uiStatus.erro )
                }
                is UIstatus.Sucesso -> {
                    alertaCarregamento.fechar()
                    val listaProdutos = uiStatus.dados
                    empresaAdapter.adicionarLista( listaProdutos )
                }
                is UIstatus.Carregando -> {
                    alertaCarregamento.exibir("Carregando Empresas")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}