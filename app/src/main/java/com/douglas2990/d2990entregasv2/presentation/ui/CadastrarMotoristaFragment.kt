package com.douglas2990.d2990entregasv2.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.douglas2990.d2990entregasv2.R
import com.douglas2990.d2990entregasv2.databinding.FragmentCadastrarMotoristaBinding
import com.douglas2990.d2990entregasv2.model.Empresa
import com.douglas2990.d2990entregasv2.presentation.viewmodel.AutenticacaoMotoristaViewModel
import com.douglas2990.d2990entregasv2.model.Motorista
import com.douglas2990.d2990entregasv2.presentation.viewmodel.CadastroMotoristaViewModel
import com.example.core.UIstatus

import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class CadastrarMotoristaFragment : Fragment() {

    private var _binding: FragmentCadastrarMotoristaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CadastroMotoristaViewModel by viewModels()

    // Lista para armazenar as empresas e recuperar o ID depois
    private var listaEmpresasLocal: List<Empresa> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastrarMotoristaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarObservadores()

        // Carrega as empresas do gestor (Douglas/Guilherme)
        viewModel.carregarEmpresas()

        binding.btnCadastrar.setOnClickListener {
            executarCadastro()
        }
    }

    private fun configurarObservadores() {
        // Observa a lista de empresas para o Dropdown
        viewModel.empresas.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Sucesso -> {
                    listaEmpresasLocal = status.dados
                    val nomesEmpresas = status.dados.map { it.nome }

                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        nomesEmpresas
                    )
                    (binding.spinnerEmpresas as? AutoCompleteTextView)?.setAdapter(adapter)
                }
                is UIstatus.Erro -> Toast.makeText(requireContext(), status.erro, Toast.LENGTH_SHORT).show()
                is UIstatus.Carregando -> { /* Opcional: mostrar progresso */ }
            }
        }

        // Observa o resultado do cadastro do motorista
        viewModel.statusCadastro.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Sucesso -> {
                    Toast.makeText(requireContext(), "Motorista cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
                is UIstatus.Erro -> Toast.makeText(requireContext(), status.erro, Toast.LENGTH_SHORT).show()
                is UIstatus.Carregando -> { /* Opcional: desabilitar botão */ }
            }
        }
    }

    private fun executarCadastro() {
        val nomeEmpresaSelecionada = binding.spinnerEmpresas.text.toString()
        val nome = binding.editCadastroNome.text.toString()
        val email = binding.editCadastroEmail.text.toString()
        val senha = binding.editCadastroSenha.text.toString()
        val telefone = binding.editCadastroTelefone.text.toString()

        // Encontra o objeto Empresa correspondente ao nome selecionado
        val empresaObj = listaEmpresasLocal.find { it.nome == nomeEmpresaSelecionada }

        if (empresaObj != null && nome.isNotEmpty() && email.isNotEmpty()) {
            val motorista = Motorista(
                nome = nome,
                email = email,
                senha = senha,
                telefone = telefone,
                idEmpresa = empresaObj.id,    // ID real do Firestore
                nomeEmpresa = empresaObj.nome // Nome para exibição
            )
            viewModel.cadastrarMotorista(motorista)
        } else {
            Toast.makeText(requireContext(), "Preencha todos os campos e selecione a empresa", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}