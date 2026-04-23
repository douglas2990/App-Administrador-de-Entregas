package com.douglas2990.d2990entregasv2.presentation.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.douglas2990.d2990entregasv2.MainActivity
import com.douglas2990.d2990entregasv2.R
import com.douglas2990.d2990entregasv2.databinding.ActivityCadastroBinding
import com.douglas2990.d2990entregasv2.model.user.Usuario
import com.douglas2990.d2990entregasv2.presentation.viewmodel.user.AutenticacaoViewModel
import com.example.core.AlertaCarregamento
import com.example.core.UIstatus
import com.example.core.esconderTeclado
import com.example.core.exibirMensagem
import com.example.core.navegarPara
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder
import kotlin.getValue

@AndroidEntryPoint
class CadastroActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityCadastroBinding.inflate(layoutInflater)
    }

    private val alertaCarregamento by lazy{
        AlertaCarregamento(this)
    }

    private val autenticacaoViewModel: AutenticacaoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(binding.root)
        inicializar()
    }

    private fun inicializar() {
        inicializarToolbar()
        inicializarEventosClique()
        inicializarObservaveis()
    }

    fun navegarPrincipal(){
        startActivity(
            Intent(this, MainActivity::class.java)
        )
    }


    private fun inicializarObservaveis() {
        autenticacaoViewModel.carregando.observe(this) { carregando ->
            if (carregando) alertaCarregamento.exibir("Fazendo seu Cadastro!")
            else alertaCarregamento.fechar()
        }

        autenticacaoViewModel.resultadoValidacao.observe(this) { res ->
            binding.editCadastroNome.error = if (res.nome) null else getString(R.string.erro_cadastro_nome)
            binding.editCadastroEmail.error = if (res.email) null else getString(R.string.erro_cadastro_email)
            binding.editCadastroSenha.error = if (res.senha) null else getString(R.string.erro_cadastro_senha)
            binding.editCadastroTelefone.error = if (res.telefone) null else getString(R.string.erro_cadastro_telefone)
        }

        // ADICIONE ESTE:
        autenticacaoViewModel.statusCadastro.observe(this) { uiStatus ->
            when (uiStatus) {
                is UIstatus.Sucesso -> {
                    navegarPara(MainActivity::class.java)
                    finish()
                }
                is UIstatus.Erro -> {
                    val erroMsg = uiStatus.erro
                    if (erroMsg.contains("pendente", ignoreCase = true)) {
                        binding.layoutEspera.visibility = View.VISIBLE // Mostra a tela de espera
                        binding.btnChamarNoZap.setOnClickListener {
                            notificarDesenvolvedor(binding.editCadastroEmail.text.toString())
                        }
                    } else {
                        exibirMensagem(erroMsg)
                    }
                }
                is UIstatus.Carregando -> { }
            }
        }
    }

   /* private fun inicializarObservaveis() {

        autenticacaoViewModel.carregando.observe(this){carregando ->
            if (carregando){
                alertaCarregamento.exibir("Fazendo seu Cadastro!")

            }else{
                alertaCarregamento.fechar()
            }

        }



        autenticacaoViewModel.resultadoValidacao
            .observe(this){resultadoValidacao ->
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
    }*/

  /*  private fun inicializarEventosClique() {
        with( binding ){
            btnCadastrar.setOnClickListener {  view ->

                view.esconderTeclado()

                editCadastroNome.clearFocus()
                editCadastroEmail.clearFocus()
                editCadastroSenha.clearFocus()
                editCadastroTelefone.clearFocus()

                val nome = editCadastroNome.text.toString()
                val email = editCadastroEmail.text.toString()
                val senha = editCadastroSenha.text.toString()
                val telefone = editCadastroTelefone.text.toString()

                val telefoneParaSalvar = binding.editCadastroTelefone.text.toString()
                    .replace(Regex("[^0-9]"), "") // Remove parênteses, espaços e traços

*//*                val usuario = Usuario(
                    email, senha, nome, telefone
                ) *//*

                val usuario = Usuario(
                    email, senha, nome, telefoneParaSalvar
                )
                autenticacaoViewModel.cadastrarUsuario( usuario ){ uiStatus ->
                    when( uiStatus ){
                        is UIstatus.Sucesso -> {
                            navegarPara( MainActivity::class.java )
                        }
                        is UIstatus.Erro ->{
                            exibirMensagem( uiStatus.erro)
                        }
                        is UIstatus.Carregando -> {

                        }
                    }

                }
            }
        }
    }*/

    private fun inicializarEventosClique() {
        with(binding) {
            btnCadastrar.setOnClickListener { view ->
                view.esconderTeclado()

                // Limpa focos para validar
                listOf(editCadastroNome, editCadastroEmail, editCadastroSenha, editCadastroTelefone).forEach { it.clearFocus() }

                val telefoneRaw = editCadastroTelefone.text.toString().replace(Regex("[^0-9]"), "")

                val usuario = Usuario(
                    email = editCadastroEmail.text.toString(),
                    senha = editCadastroSenha.text.toString(),
                    nome = editCadastroNome.text.toString(),
                    //telefone = editCadastroTelefone.text.toString().replace(Regex("[^0-9]"), "")
                    telefone = telefoneRaw
                )

                // APENAS CHAMA A FUNÇÃO. Não trate o resultado aqui dentro.
                autenticacaoViewModel.cadastrarUsuario(usuario)
            }
        }
    }

    private fun inicializarToolbar() {
        val toolbar = binding.includeTbPrincipal.tbPrincipal
        setSupportActionBar( toolbar )

        supportActionBar?.apply {
            title = "Cadastro de usuário"
            setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun notificarDesenvolvedor(emailSolicitante: String) {
        val meuNumero = "55119XXXXXXXX" // Seu número com DDI e DDD
        val mensagem = "Olá Douglas, solicito acesso para o e-mail: $emailSolicitante"
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$meuNumero&text=${URLEncoder.encode(mensagem, "UTF-8")}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}