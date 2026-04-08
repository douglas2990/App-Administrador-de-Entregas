package com.example.core.usecase

import android.net.Uri

import com.example.core.UIstatus
import com.example.core.model.Rota
import com.example.core.repository.IRotaRepository
import javax.inject.Inject

class RotaUseCase @Inject constructor(
    private val repository: IRotaRepository
) {

    // 1. SALVAR COM VALIDAÇÃO DE DATA CORRIGIDA
    suspend fun salvarRota(rota: Rota): UIstatus<String> {
        if (rota.os.isEmpty()) return UIstatus.Erro("O número da OS é obrigatório")
        if (rota.nomeEmpresaDestino.isEmpty()) return UIstatus.Erro("O nome da empresa é obrigatório")
        if (rota.endereco.isEmpty()) return UIstatus.Erro("O endereço é obrigatório")
        if (rota.idMotorista.isEmpty()) return UIstatus.Erro("Selecione um motorista")

        // CORREÇÃO DO ERRO:
        // Verificamos se dataPrevista é nula OU menor/igual a zero separadamente
        val data = rota.dataPrevista
        if (data == null || data <= 0L) {
            return UIstatus.Erro("A data prevista de entrega é obrigatória")
        }

        return repository.salvar(rota)
    }

    // 2. NOVOS MÉTODOS PARA O CALENDÁRIO DO MOTORISTA

    // Busca os dias que possuem entregas (ex: 10/04, 12/04)
    suspend fun listarDatasComRotas(idMotorista: String) =
        repository.listarDatasComRotas(idMotorista)

    // Busca as entregas de um dia específico que o motorista clicou
    suspend fun listarPorDataEMotorista(idMotorista: String, data: Long) =
        repository.listarPorDataEMotorista(idMotorista, data)

    // 3. MÉTODOS EXISTENTES

    suspend fun listarRotasMotorista(idMotorista: String) =
        repository.listarPorMotorista(idMotorista)

    suspend fun listarTodasAsRotas() =
        repository.listarTodas()

    suspend fun concluirRota(idRota: String, imageUri: Uri) =
        repository.finalizarRotaComSucesso(idRota, imageUri)

    suspend fun reportarProblema(idRota: String, motivo: String, imageUri: Uri? = null) =
        repository.reportarProblemaRota(idRota, motivo, imageUri)

    suspend fun excluirRota(idRota: String) =
        repository.remover(idRota)
}