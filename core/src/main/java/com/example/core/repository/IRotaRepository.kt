package com.example.core.repository

import android.net.Uri
import com.example.core.UIstatus
import com.example.core.model.Motorista
import com.example.core.model.Rota

interface IRotaRepository {

    suspend fun listarDatasComRotas(idMotorista: String): UIstatus<List<Long>>
    suspend fun listarPorDataEMotorista(idMotorista: String, data: Long): UIstatus<List<Rota>>
    suspend fun salvar(rota: Rota): UIstatus<String>
    suspend fun listarPorMotorista(idMotorista: String): UIstatus<List<Rota>>
    suspend fun listarTodas(): UIstatus<List<Rota>>
    suspend fun atualizarStatus(idRota: String, status: String, observacao: String? = null): UIstatus<Boolean>
    suspend fun enviarComprovante(idRota: String, imageUri: Uri): UIstatus<String>
    suspend fun remover(idRota: String): UIstatus<Boolean>

    suspend fun finalizarRotaComSucesso(idRota: String, imageUri: Uri): UIstatus<String>
    suspend fun reportarProblemaRota(idRota: String, motivo: String, imageUri: Uri?): UIstatus<String>

    fun listarPorMotoristaRealTime(
        idMotorista: String,
        onResult: (UIstatus<List<Rota>>) -> Unit
    )

    fun listarPorMotoristaEDataRealTime(uid: String, data: String, callback: (UIstatus<List<Rota>>) -> Unit)

    suspend fun buscarMotorista(uid: String): Motorista?
}
