package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import com.douglas2990.d2990entregasv2.model.Motorista

interface IAutenticacaoMotoristaRepository {
    suspend fun cadastrarMotorista(motorista: Motorista): Boolean
}