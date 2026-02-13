package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import com.douglas2990.d2990entregasv2.model.Motorista
import com.example.core.UIstatus

interface IMotoristaRepository {
    suspend fun salvar(motorista: Motorista): UIstatus<String>
    suspend fun listar(): UIstatus<List<Motorista>>
    suspend fun remover(idMotorista: String): UIstatus<Boolean>
}