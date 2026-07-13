package com.douglas2990.d2990entregasv2.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.douglas2990.d2990entregasv2.domain.usecase.RotaUseCase
import com.douglas2990.d2990entregasv2.model.Rota
import com.example.core.UIstatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RotaViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val rotaUseCase: RotaUseCase = mockk()
    private lateinit var viewModel: RotaViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RotaViewModel(rotaUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun salvarRota_quandoSucesso_deveMudarEstadoParaSucesso() = runTest {
        val rota = Rota(os = "123")
        coEvery { rotaUseCase.salvarRota(rota) } returns UIstatus.Sucesso("Sucesso")

        viewModel.salvarRota(rota)

        assertTrue(viewModel.statusSalvar.value is UIstatus.Sucesso)
        assertEquals("Sucesso", (viewModel.statusSalvar.value as UIstatus.Sucesso).dados)
    }

    @Test
    fun salvarRota_quandoErro_deveMudarEstadoParaErro() = runTest {
        val rota = Rota(os = "123")
        coEvery { rotaUseCase.salvarRota(rota) } returns UIstatus.Erro("Falha")

        viewModel.salvarRota(rota)

        assertTrue(viewModel.statusSalvar.value is UIstatus.Erro)
        assertEquals("Falha", (viewModel.statusSalvar.value as UIstatus.Erro).erro)
    }

    @Test
    fun listarRotasMotorista_deveRetornarListaDeRotas() = runTest {
        val idMotorista = "motorista1"
        val listaRotas = listOf(Rota(os = "101"), Rota(os = "102"))
        coEvery { rotaUseCase.listarRotasMotorista(idMotorista) } returns UIstatus.Sucesso(listaRotas)

        viewModel.listarRotasMotorista(idMotorista)

        assertTrue(viewModel.rotas.value is UIstatus.Sucesso)
        assertEquals(2, (viewModel.rotas.value as UIstatus.Sucesso).dados?.size)
    }
}
