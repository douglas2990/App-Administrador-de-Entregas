package com.douglas2990.d2990entregasv2.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.ICadastroAcessoRepository
import com.example.core.UIstatus
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CadastroAcessoViewModelTest {

    // Regra necessária para testar componentes que usam LiveData/Architecture Components localmente
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Criamos um mock (dublê) do seu repositório. Não usaremos o Firebase real aqui!
    private val repository: ICadastroAcessoRepository = mockk(relaxed = true)

    private lateinit var viewModel: CadastroAcessoViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        // Redireciona o Dispatchers.Main das Coroutines para rodar na nossa Thread de teste local
        Dispatchers.setMain(testDispatcher)

        // Inicializa a ViewModel injetando o repositório falso
        viewModel = CadastroAcessoViewModel(repository)
    }

    @After
    fun tearDown() {
        // Limpa o modificador de Dispatcher após a execução do teste
        Dispatchers.resetMain()
    }

    @Test
    fun cadastrarNovoMotorista_quandoSenhaForCurta_deveRetornarErroDeValidacao() {
        // GIVEN (Dado que) - Definimos entradas inválidas (senha com menos de 6 caracteres)
        val email = "motorista@hotmail.com"
        val senhaInvalida = "123"
        val nome = "Douglas Oliveira"

        // WHEN (Quando) - A ação da ViewModel é executada
        viewModel.cadastrarNovoMotorista(email, senhaInvalida, nome)

        // THEN (Então) - Verificamos se o StateFlow emitiu o erro esperado de validação
        val statusAtual = viewModel.status.value

        assert(statusAtual is UIstatus.Erro)
        assertEquals(
            "Preencha todos os campos. A senha deve ter 6+ caracteres.",
            (statusAtual as UIstatus.Erro).erro
        )
    }
}