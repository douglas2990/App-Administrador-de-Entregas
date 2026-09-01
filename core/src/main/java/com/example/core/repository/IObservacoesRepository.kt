package com.example.core.repository

import com.example.core.model.ObservacaoCliente
import com.example.core.UIstatus

interface IObservacoesRepository {
    suspend fun salvar(observacao: ObservacaoCliente): UIstatus<String>
    suspend fun listarTodas(): UIstatus<List<ObservacaoCliente>>
}
