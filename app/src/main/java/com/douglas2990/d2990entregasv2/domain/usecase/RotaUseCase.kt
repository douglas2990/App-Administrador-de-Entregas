package com.douglas2990.d2990entregasv2.domain.usecase

import android.net.Uri
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IRotaRepository
import com.douglas2990.d2990entregasv2.model.ItemAgendaAdmin
import com.douglas2990.d2990entregasv2.model.Rota
import com.example.core.UIstatus
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

    suspend fun listarDatasComRotasAdmin(idMotorista: String) =
        repository.listarDatasComRotasAdmin(idMotorista)

    suspend fun listarDatasComStatusAdmin(idMotorista: String): UIstatus<List<ItemAgendaAdmin>> {
        return repository.listarDatasComStatusAdmin(idMotorista)
    }

    suspend fun arquivarRotaPorDia(idMotorista: String, data: Long): UIstatus<Boolean> {
        return repository.arquivarRotaPorDia(idMotorista, data)
    }

/*    suspend fun listarRotasArquivadas(idMotorista: String) =
        repository.listarRotasArquivadas(idMotorista)*/

    suspend fun listarDatasArquivadas(idMotorista: String): UIstatus<List<ItemAgendaAdmin>> {
        return repository.listarDatasArquivadas(idMotorista)
    }

    suspend fun listarAgendaArquivada(idMotorista: String) = repository.listarAgendaArquivadaAdmin(idMotorista)

    // Lista apenas o que está "vivo" (arquivada == false)
    suspend fun listarAgendaAtiva(idMotorista: String) =
        repository.listarAgendaAtivaAdmin(idMotorista)

    // Lista apenas o histórico (arquivada == true) - você já adicionou como listarAgendaArquivada
    //suspend fun listarAgendaArquivada(idMotorista: String) = repository.listarAgendaArquivadaAdmin(idMotorista)

    // Busca as rotas internas de uma data que já foi arquivada
    suspend fun listarRotasArquivadasPorData(idMotorista: String, data: Long) =
        repository.listarRotasArquivadasPorData(idMotorista, data)

    // Executa o processo de "guardar no baú" (mudar campo arquivada para true)
    suspend fun executarArquivamento(idMotorista: String, data: Long) =
        repository.executarArquivamentoDaData(idMotorista, data)
}