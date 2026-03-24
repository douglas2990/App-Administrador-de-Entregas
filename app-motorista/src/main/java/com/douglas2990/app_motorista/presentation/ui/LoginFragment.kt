package com.douglas2990.app_motorista.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.douglas2990.app_motorista.R
import com.douglas2990.app_motorista.databinding.FragmentLoginBinding
import com.douglas2990.app_motorista.presentation.viewmodel.LoginViewModel
import com.example.core.UIstatus
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // COMANDO PARA DESLOGAR (Apenas para teste ou reset)
        FirebaseAuth.getInstance().signOut()

        // Se o usuário já estiver logado, vai direto para a tela de Rotas
        if (viewModel.usuarioLogado()) {
            irParaTelaRotas()
        }

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            viewModel.login(email, senha)
        }
    }

    private fun setupObservers() {
        viewModel.loginStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnLogin.isEnabled = false
                }
                is UIstatus.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    irParaTelaRotas()
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(context, status.erro, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irParaTelaRotas() {
         findNavController().navigate(R.id.action_loginFragment_to_rotasMotoristaFragment)
        Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
