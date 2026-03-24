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
import com.douglas2990.app_motorista.databinding.FragmentRotasMotoristaBinding
import com.example.core.model.Rota
import com.douglas2990.app_motorista.presentation.ui.adapter.RotaAdapter
import com.douglas2990.app_motorista.presentation.viewmodel.RotasMotoristaViewModel
import com.example.core.UIstatus
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RotasMotoristaFragment : Fragment() {

    private var _binding: FragmentRotasMotoristaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RotasMotoristaViewModel by viewModels()
    private lateinit var rotaAdapter: RotaAdapter

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

        // Dispara a busca das rotas vinculadas ao UID do motorista logado
        viewModel.observarMinhasRotas()
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
                    val lista = status.dados ?: emptyList()
                    rotaAdapter.submitList(lista)

                    // Gerencia o texto de lista vazia
                    binding.textListaVazia.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
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