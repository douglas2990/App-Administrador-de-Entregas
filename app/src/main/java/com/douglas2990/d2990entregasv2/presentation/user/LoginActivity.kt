package com.douglas2990.d2990entregasv2.presentation.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.douglas2990.d2990entregasv2.MainActivity
import com.douglas2990.d2990entregasv2.R
import com.douglas2990.d2990entregasv2.databinding.ActivityLoginBinding
import com.douglas2990.d2990entregasv2.model.user.Usuario
import com.douglas2990.d2990entregasv2.presentation.viewmodel.user.AutenticacaoViewModel
import com.example.core.AlertaCarregamento
import com.example.core.UIstatus
import com.example.core.esconderTeclado
import com.example.core.exibirMensagem
import com.example.core.navegarPara
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val alertaCarregamento by lazy{
        AlertaCarregamento(this)
    }

    private val autenticacaoViewModel: AutenticacaoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime()) // IME é o teclado
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, ime.bottom)
            insets
        }

        //Thread.sleep(3000)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{
            //verificar usuario logado
            val usuarioLogado = autenticacaoViewModel.verificarUsuarioLogado()
            if ( usuarioLogado ) {
                navegarPara( MainActivity::class.java )
            }
            false
        }

        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(binding.root)
        inicializar()
        FirebaseAuth.getInstance().signOut()
    }

    private fun inicializar() {
        inicializarEventoClique()
        inicializarObservaveis()
    }

    override fun onStart() {
        super.onStart()
        autenticacaoViewModel.verificarUsuarioLogado()
    }

    fun navegarPrincipal(){
        startActivity(
            Intent(this, MainActivity::class.java)
        )
    }

    private fun inicializarObservaveis() {

        autenticacaoViewModel.carregando.observe(this){carregando ->
            if (carregando){
                alertaCarregamento.exibir("Efetuando login!")

            }else{
                alertaCarregamento.fechar()
            }

        }



        autenticacaoViewModel.resultadoValidacao
            .observe(this){resultadoValidacao ->
                with( binding ){

                    editLoginEmail.error =
                        if (resultadoValidacao.email) null else getString(R.string.erro_cadastro_email)

                    editLoginSenha.error =
                        if (resultadoValidacao.senha) null else getString(R.string.erro_cadastro_senha)

                }
            }
    }

    private fun inicializarEventoClique() {
        with( binding ){
            textCadastro.setOnClickListener {
                startActivity(
                    Intent(applicationContext, CadastroActivity::class.java)
                )
            }

            //editLoginEmail.requestFocus()

            btnLogin.setOnClickListener {view ->

                view.esconderTeclado()

                editLoginEmail.clearFocus()
                editLoginSenha.clearFocus()

                val email = editLoginEmail.text.toString()
                val senha = editLoginSenha.text.toString()
                val usuario = Usuario(
                    email, senha
                )
                autenticacaoViewModel.logarUsuario( usuario ){ uiStatus ->
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
    }
}