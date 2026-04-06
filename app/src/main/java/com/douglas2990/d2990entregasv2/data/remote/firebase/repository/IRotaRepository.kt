package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import com.douglas2990.d2990entregasv2.model.Rota
import com.example.core.UIstatus
import android.net.Uri

interface IRotaRepository {
    suspend fun salvar(rota: Rota): UIstatus<String>
    suspend fun listarPorMotorista(idMotorista: String): UIstatus<List<Rota>>
    suspend fun listarTodas(): UIstatus<List<Rota>>
    suspend fun atualizarStatus(idRota: String, status: String, observacao: String? = null): UIstatus<Boolean>
    suspend fun enviarComprovante(idRota: String, imageUri: Uri): UIstatus<String>
    suspend fun remover(idRota: String): UIstatus<Boolean>

    suspend fun finalizarRotaComSucesso(idRota: String, imageUri: Uri): UIstatus<String>
    suspend fun reportarProblemaRota(idRota: String, motivo: String, imageUri: Uri?): UIstatus<String>
}
