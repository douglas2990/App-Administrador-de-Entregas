package com.douglas2990.d2990entregasv2.domain.usecase

import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IRotaRepository
import com.douglas2990.d2990entregasv2.model.Rota
import com.example.core.UIstatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RotaUseCaseTest {

    private val repository: IRotaRepository = mockk()
    private lateinit var useCase: RotaUseCase

    @Before
    fun setup() {
        useCase = RotaUseCase(repository)
    }

    @Test
    fun salvarRota_quandoOSEstiverVazia_deveRetornarErro() = runTest {
        val rota = Rota(os = "", nomeEmpresaDestino = "Empresa", endereco = "Rua A", idMotorista = "1", dataPrevista = 123456L)
        val resultado = useCase.salvarRota(rota)
        
        assertTrue(resultado is UIstatus.Erro)
        assertEquals("O número da OS é obrigatório", (resultado as UIstatus.Erro).erro)
    }

    @Test
    fun salvarRota_quandoEmpresaEstiverVazia_deveRetornarErro() = runTest {
        val rota = Rota(os = "123", nomeEmpresaDestino = "", endereco = "Rua A", idMotorista = "1", dataPrevista = 123456L)
        val resultado = useCase.salvarRota(rota)
        
        assertTrue(resultado is UIstatus.Erro)
        assertEquals("O nome da empresa é obrigatório", (resultado as UIstatus.Erro).erro)
    }

    @Test
    fun salvarRota_quandoEnderecoEstiverVazio_deveRetornarErro() = runTest {
        val rota = Rota(os = "123", nomeEmpresaDestino = "Empresa", endereco = "", idMotorista = "1", dataPrevista = 123456L)
        val resultado = useCase.salvarRota(rota)
        
        assertTrue(resultado is UIstatus.Erro)
        assertEquals("O endereço é obrigatório", (resultado as UIstatus.Erro).erro)
    }

    @Test
    fun salvarRota_quandoMotoristaNaoSelecionado_deveRetornarErro() = runTest {
        val rota = Rota(os = "123", nomeEmpresaDestino = "Empresa", endereco = "Rua A", idMotorista = "", dataPrevista = 123456L)
        val resultado = useCase.salvarRota(rota)
        
        assertTrue(resultado is UIstatus.Erro)
        assertEquals("Selecione um motorista", (resultado as UIstatus.Erro).erro)
    }

    @Test
    fun salvarRota_quandoDataInvalida_deveRetornarErro() = runTest {
        val rota = Rota(os = "123", nomeEmpresaDestino = "Empresa", endereco = "Rua A", idMotorista = "1", dataPrevista = 0L)
        val resultado = useCase.salvarRota(rota)
        
        assertTrue(resultado is UIstatus.Erro)
        assertEquals("A data prevista de entrega é obrigatória", (resultado as UIstatus.Erro).erro)
    }

    @Test
    fun salvarRota_quandoDadosValidos_deveChamarRepository() = runTest {
        val rota = Rota(os = "123", nomeEmpresaDestino = "Empresa", endereco = "Rua A", idMotorista = "1", dataPrevista = 123456L)
        coEvery { repository.salvar(rota) } returns UIstatus.Sucesso("Salvo")
        
        val resultado = useCase.salvarRota(rota)
        
        assertTrue(resultado is UIstatus.Sucesso)
        assertEquals("Salvo", (resultado as UIstatus.Sucesso).dados)
    }
}
