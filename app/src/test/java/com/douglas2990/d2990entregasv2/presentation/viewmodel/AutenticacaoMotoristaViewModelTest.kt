package com.douglas2990.d2990entregasv2.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IEmpresaRepository
import com.douglas2990.d2990entregasv2.domain.usecase.SalvarMotoristaUseCase
import com.douglas2990.d2990entregasv2.model.Empresa
import com.example.core.UIstatus
import io.mockk.coEvery
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AutenticacaoMotoristaViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Mockamos as duas dependências que a sua ViewModel pede no construtor
    private val salvarMotoristaUseCase: SalvarMotoristaUseCase = mockk(relaxed = true)
    private val empresaRepository: IEmpresaRepository = mockk(relaxed = true)

    private lateinit var viewModel: AutenticacaoMotoristaViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AutenticacaoMotoristaViewModel(salvarMotoristaUseCase, empresaRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun carregarEmpresasParaSpinner_quandoRepositoryResponderSucesso_deveMudarEstadoParaSucesso() = runTest {
        // GIVEN (Dado que) - Criamos uma lista falsa de empresas
        val listaFalsaEmpresas = listOf(
            Empresa(id = "1", nome = "Entrega Rápida Diadema", cnpj = "123456"),
            Empresa(id = "2", nome = "Logística ABC", cnpj = "789012")
        )
        val respostaFalsa = UIstatus.Sucesso(listaFalsaEmpresas)

        // CORREÇÃO AQUI: Mudamos para coEvery porque 'listar' é uma suspend fun
        coEvery {
            empresaRepository.listar(any())
        } answers {
            // Captura o primeiro argumento, que é o callback lambda
            val callback = firstArg<(UIstatus<List<Empresa>>) -> Unit>()
            callback.invoke(respostaFalsa)
        }

        // WHEN (Quando) - Chamamos a função na ViewModel
        viewModel.carregarEmpresasParaSpinner()

        // THEN (Então) - O LiveData deve conter a nossa lista de sucesso capturada
        val statusAtual = viewModel.statusEmpresas.value

        assert(statusAtual is UIstatus.Sucesso)

        val dadosRetornados = (statusAtual as UIstatus.Sucesso).dados
        assertEquals(2, dadosRetornados?.size)
        assertEquals("Entrega Rápida Diadema", dadosRetornados?.get(0)?.nome)
    }
}