package com.douglas2990.d2990entregasv2.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.douglas2990.d2990entregasv2.R
import com.douglas2990.d2990entregasv2.databinding.FragmentCadastrarEmpresaBinding
import com.douglas2990.d2990entregasv2.databinding.FragmentEmpresasCadastradasBinding
import com.douglas2990.d2990entregasv2.model.Empresa
import com.douglas2990.d2990entregasv2.presentation.adapter.EmpresaAdapter
import com.douglas2990.d2990entregasv2.presentation.viewmodel.CadastroEmpresaViewModel
import com.example.core.AlertaCarregamento
import com.example.core.UIstatus
import com.example.core.esconderTeclado
import com.example.core.exibirMensagem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint





@AndroidEntryPoint
class CadastrarEmpresaFragment : Fragment() {
    private var _binding: FragmentCadastrarEmpresaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val cadastroEmpresaViewModel: CadastroEmpresaViewModel by viewModels()

    private val args: CadastrarEmpresaFragmentArgs by navArgs()

    private var idEmpresaEdicao: String? = null

    private val alertaCarregamento by lazy {
        AlertaCarregamento(requireContext())
    }
    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCadastrarEmpresaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializar()

        verificarEdicao()
    }

    private fun verificarEdicao() {
        args.empresa?.let { empresa ->
            idEmpresaEdicao = empresa.id // Guarda o ID para o ViewModel saber que é edição

            with(binding) {
                editCadastroEmpresa.setText(empresa.nome)
                editCadastroEmailEmpresa.setText(empresa.email)
                editCadastrarCnpjlEmpresa.setText(empresa.cnpj)
                editCadastroTelefoneEmpresa.setText(empresa.telefone)

                btnCadastrar.text = "Atualizar Empresa" // Muda o texto do botão

            }
        }
    }

    private fun inicializar() {
        //inicializarEventosClique()
        //inicializarObservaveis()
        inicializarEventosClique3()
        inicializarObservaveis2()
    }



    private fun inicializarObservaveis() {

    }


    private fun inicializarObservaveis2() {
        cadastroEmpresaViewModel.uiStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> alertaCarregamento.exibir("Processando...")
                is UIstatus.Sucesso -> {
                    alertaCarregamento.fechar()
                    exibirMensagem("Empresa salva!")
                    // Limpar campos ou fechar tela
                }
                is UIstatus.Erro -> {
                    alertaCarregamento.fechar()
                    exibirMensagem(status.erro) // Aqui aparecerá "CNPJ inválido"
                }
            }
        }
    }

    private fun inicializarEventosClique2() {
        binding.btnCadastrar.setOnClickListener { view ->
            view.esconderTeclado()

            // Pegando os dados sem a máscara para não sujar o banco
            val empresa = Empresa(
                nome = binding.editCadastroEmpresa.text.toString(),
                email = binding.editCadastroEmailEmpresa.text.toString(),
                cnpj = binding.editCadastrarCnpjlEmpresa.text.toString().replace(Regex("[^\\d]"), ""),
                telefone = binding.editCadastroTelefoneEmpresa.unMaskedText ?: ""
            )

            if (validarCamposEmBranco(empresa)) {
                cadastroEmpresaViewModel.salvarCadastroEmpresa2(empresa)
            } else {
                exibirMensagem("Preencha todos os campos")
            }
        }
    }


    private fun inicializarEventosClique3() {
        with( binding ){
            btnCadastrar.setOnClickListener { view ->

                view.esconderTeclado()

                //Remover Focus
                editCadastroEmpresa.clearFocus()
                editCadastroEmailEmpresa.clearFocus()
                editCadastrarCnpjlEmpresa.clearFocus()
                editCadastroTelefoneEmpresa.clearFocus()


                val nome = editCadastroEmpresa.text.toString()
                val email = editCadastroEmailEmpresa.text.toString()
                val cnpj = editCadastrarCnpjlEmpresa.text.toString().replace(Regex("[^\\d]"), "")
                val telefone = editCadastroTelefoneEmpresa.text.toString()

                val empresa = Empresa(
                    id = idEmpresaEdicao ?: "",
                    nome = nome,
                    email = email,
                    cnpj = cnpj,
                    telefone = telefone
                )
                if (validarCamposEmBranco(empresa)) {
                    cadastroEmpresaViewModel.salvarCadastroEmpresa2(empresa)
                } else {
                    exibirMensagem("Preencha todos os campos")
                }

            }
            btnMostrarEmpresa.setOnClickListener {
                findNavController().navigate(R.id.empresasCadastradasFragment)
            }
        }
    }

    private fun inicializarEventosClique() {
        with( binding ){
            btnCadastrar.setOnClickListener { view ->

                view.esconderTeclado()

                //Remover Focus
                editCadastroEmpresa.clearFocus()
                editCadastroEmailEmpresa.clearFocus()
                editCadastrarCnpjlEmpresa.clearFocus()
                editCadastroTelefoneEmpresa.clearFocus()


                val nome = editCadastroEmpresa.text.toString()
                val email = editCadastroEmailEmpresa.text.toString()
                val cnpj = editCadastrarCnpjlEmpresa.text.toString().replace(Regex("[^\\d]"), "")
                val telefone = editCadastroTelefoneEmpresa.text.toString()

                val empresa = Empresa(
                    nome = nome, email = email, cnpj = cnpj, telefone = telefone
                )
                cadastroEmpresaViewModel.salvarCadastroEmpresa( empresa ){ uiStatus ->
                    when( uiStatus ){
                        is UIstatus.Sucesso -> {
                            exibirMensagem("Empresa Cadastrada com sucesso")
                        }
                        is UIstatus.Erro -> {
                            exibirMensagem( uiStatus.erro )
                        }
                        is UIstatus.Carregando -> {}
                    }
                }

            }
        }
    }

    private fun validarCamposEmBranco(empresa: Empresa): Boolean {
        return empresa.nome.isNotBlank() &&
                empresa.email.isNotBlank() &&
                empresa.cnpj.isNotBlank() &&
                empresa.telefone.isNotBlank()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

