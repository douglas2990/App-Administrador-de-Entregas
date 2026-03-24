package com.example.core.repository

import android.net.Uri
import com.example.core.UIstatus
import com.example.core.model.Rota

interface IRotaRepository {
    suspend fun salvar(rota: Rota): UIstatus<String>
    suspend fun listarPorMotorista(idMotorista: String): UIstatus<List<Rota>>
    suspend fun listarTodas(): UIstatus<List<Rota>>
    suspend fun atualizarStatus(idRota: String, status: String, observacao: String? = null): UIstatus<Boolean>
    suspend fun enviarComprovante(idRota: String, imageUri: Uri): UIstatus<String>
    suspend fun remover(idRota: String): UIstatus<Boolean>

    fun listarPorMotoristaRealTime(
        idMotorista: String,
        onResult: (UIstatus<List<Rota>>) -> Unit
    )
}
