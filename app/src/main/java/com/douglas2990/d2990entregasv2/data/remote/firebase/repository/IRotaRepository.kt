package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import android.net.Uri
import com.douglas2990.d2990entregasv2.model.Rota
import com.example.core.UIstatus

interface IRotaRepository {

    // --- MÉTODOS DO CALENDÁRIO (MOTORISTA) ---

    // Busca as datas únicas para marcar no calendário (Usa Long/Timestamp)
    suspend fun listarDatasComRotas(idMotorista: String): UIstatus<List<Long>>

    // Admin vê as datas das rotas que ele criou para o motorista
    suspend fun listarDatasComRotasAdmin(idMotorista: String): UIstatus<List<Long>>

    // Busca as rotas de um dia específico (Usa Long/Timestamp - Versão One-shot)
    suspend fun listarPorDataEMotorista(idMotorista: String, data: Long): UIstatus<List<Rota>>

    // NOVIDADE: Busca em tempo real usando a data formatada (O que o seu Fragment usa agora)
    fun listarPorMotoristaEDataRealTime(
        uid: String,
        data: String,
        callback: (UIstatus<List<Rota>>) -> Unit
    )

    // --- MÉTODOS DE ESCRITA E LISTAGEM (ADMIN) ---

    suspend fun salvar(rota: Rota): UIstatus<String>

    suspend fun listarPorMotorista(idMotorista: String): UIstatus<List<Rota>>

    suspend fun listarTodas(): UIstatus<List<Rota>>

    suspend fun remover(idRota: String): UIstatus<Boolean>

    // --- MÉTODOS DE MONITORAMENTO ---

    fun listarPorMotoristaRealTime(
        idMotorista: String,
        onResult: (UIstatus<List<Rota>>) -> Unit
    )

    // --- MÉTODOS DE STATUS E FINALIZAÇÃO ---

    suspend fun atualizarStatus(idRota: String, status: String, observacao: String?): UIstatus<Boolean>

    suspend fun enviarComprovante(idRota: String, imageUri: Uri): UIstatus<String>

    suspend fun finalizarRotaComSucesso(idRota: String, imageUri: Uri): UIstatus<String>

    suspend fun reportarProblemaRota(idRota: String, motivo: String, imageUri: Uri?): UIstatus<String>
}