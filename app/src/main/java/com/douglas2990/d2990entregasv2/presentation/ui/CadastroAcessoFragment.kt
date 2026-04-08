package com.douglas2990.d2990entregasv2.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.douglas2990.d2990entregasv2.databinding.FragmentCadastroAcessoBinding
import com.douglas2990.d2990entregasv2.presentation.viewmodel.CadastroAcessoViewModel
import com.example.core.UIstatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CadastroAcessoFragment : Fragment() {

    // View Binding para acessar o XML de forma segura
    private var _binding: FragmentCadastroAcessoBinding? = null
    private val binding get() = _binding!!

    // Injeção do ViewModel via Hilt
    private val viewModel: CadastroAcessoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Infla o layout XML profissional que criamos
        _binding = FragmentCadastroAcessoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura a ação do botão principal
        binding.btnCadastrar.setOnClickListener {
            // Captura os dados dos campos de texto (TextInputEditText)
            val nome = binding.editNome.text.toString()
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            // Chama a lógica de negócio no ViewModel
            viewModel.cadastrarNovoMotorista(email, senha, nome)
        }

        // Inicia a observação dos estados da tela
        observarViewModel()
    }

    private fun observarViewModel() {
        // Usa o lifecycleScope para observar o StateFlow de forma segura
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.status.collect { status ->
                when (status) {
                    is UIstatus.Carregando -> {
                        // Mostra o ProgressBar horizontal no topo e desabilita o botão
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnCadastrar.isEnabled = false
                        binding.btnCadastrar.text = "Processando..."
                    }
                    is UIstatus.Sucesso -> {
                        // Esconde o carregamento e reabilita o botão
                        binding.progressBar.visibility = View.GONE
                        binding.btnCadastrar.isEnabled = true
                        binding.btnCadastrar.text = "Gerar Acesso"

                        // Feedback profissional e limpa a tela para o próximo cadastro
                        Toast.makeText(requireContext(), "Acesso criado com sucesso! O motorista já pode logar.", Toast.LENGTH_LONG).show()
                        limparCampos()
                        viewModel.resetarStatus() // Evita que a mensagem reapareça na rotação
                    }
                    is UIstatus.Erro -> {
                        // Esconde o carregamento e reabilita o botão
                        binding.progressBar.visibility = View.GONE
                        binding.btnCadastrar.isEnabled = true
                        binding.btnCadastrar.text = "Gerar Acesso"

                        // Mostra a mensagem de erro que veio do repositório/validação
                        Toast.makeText(requireContext(), "Falha: ${status.erro}", Toast.LENGTH_LONG).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun limparCampos() {
        // Limpa os campos de texto para novo cadastro
        binding.editNome.text?.clear()
        binding.editEmail.text?.clear()
        binding.editSenha.text?.clear()
        // Opcional: Tira o foco do último campo
        binding.editSenha.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Evita memory leaks limpando a referência do binding
        _binding = null
    }
}