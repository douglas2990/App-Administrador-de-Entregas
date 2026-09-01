package com.douglas2990.app_motorista.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.douglas2990.app_motorista.databinding.FragmentCadastrarObservacaoBinding
import com.douglas2990.app_motorista.presentation.viewmodel.ObservacoesViewModel
import com.example.core.UIstatus
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CadastrarObservacaoFragment : Fragment() {

    private var _binding: FragmentCadastrarObservacaoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ObservacoesViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastrarObservacaoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVoltar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSalvar.setOnClickListener {
            val nomeCliente = binding.editNomeCliente.text.toString()
            val endereco = binding.editEndereco.text.toString()
            val relato = binding.editRelato.text.toString()
            
            val user = firebaseAuth.currentUser
            if (user != null) {
                viewModel.salvarObservacao(
                    nomeCliente = nomeCliente,
                    endereco = endereco,
                    relato = relato,
                    idMotorista = user.uid,
                    nomeMotorista = user.displayName ?: user.email ?: "Motorista"
                )
            } else {
                Toast.makeText(requireContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            }
        }

        observarStatus()
    }

    private fun observarStatus() {
        viewModel.statusSalvar.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSalvar.isEnabled = false
                }
                is UIstatus.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), status.dados, Toast.LENGTH_SHORT).show()
                    viewModel.resetStatusSalvar()
                    findNavController().popBackStack()
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSalvar.isEnabled = true
                    Toast.makeText(requireContext(), status.erro, Toast.LENGTH_LONG).show()
                }
                null -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSalvar.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
