package com.douglas2990.d2990entregasv2.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.douglas2990.d2990entregasv2.R
import com.douglas2990.d2990entregasv2.databinding.FragmentCadastrarMotoristaBinding
import com.douglas2990.d2990entregasv2.presentation.viewmodel.AutenticacaoMotoristaViewModel
import com.douglas2990.d2990entregasv2.model.Motorista

import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class CadastrarMotoristaFragment : Fragment() {
    private var _binding: FragmentCadastrarMotoristaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding !!

    private val autenticacaoMotoristaViewModel: AutenticacaoMotoristaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCadastrarMotoristaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializar()


    }

    private fun inicializar() {
        inicializarEventosClique()
        inicializarObservaveis()
    }

    fun navegarPrincipal(){
        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
    }

    private fun inicializarObservaveis() {

        autenticacaoMotoristaViewModel.sucesso.observe(viewLifecycleOwner){sucesso ->
            if (sucesso){
                Toast.makeText(context, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
                navegarPrincipal()

            }else{
                Toast.makeText(context,"Erro ao realizar cadastro",Toast.LENGTH_SHORT).show()
            }

        }

        autenticacaoMotoristaViewModel.resultadoValidacao
            .observe(viewLifecycleOwner){resultadoValidacao ->
                with( binding ){
                    editCadastroNome.error =
                        if (resultadoValidacao.nome) null else getString(R.string.erro_cadastro_nome)

                    editCadastroEmail.error =
                        if (resultadoValidacao.email) null else getString(R.string.erro_cadastro_email)

                    editCadastroSenha.error =
                        if (resultadoValidacao.senha) null else getString(R.string.erro_cadastro_senha)

                    editCadastroTelefone.error =
                        if (resultadoValidacao.telefone) null else getString(R.string.erro_cadastro_telefone)
                }

            }
    }

    private fun inicializarEventosClique() {
        with( binding ){
            btnCadastrar.setOnClickListener {

                val id = 0.toString()
                val nome = editCadastroNome.text.toString()
                val email = editCadastroEmail.text.toString()
                val senha = editCadastroSenha.text.toString()
                val telefone = editCadastroTelefone.text.toString()
                val empresa = editCadastroEmpresa.text.toString()

                val usuario = Motorista(
                     email = email, senha= senha, nome =nome, telefone = telefone, empresa= empresa
                )
                autenticacaoMotoristaViewModel.cadastrarMotorista( usuario )
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}