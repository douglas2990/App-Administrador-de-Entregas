package com.example.core.repository

import com.example.core.UIstatus
import com.example.core.model.Motorista

interface IMotoristaRepository {
    // Atualizado para receber as credenciais de acesso
    suspend fun salvar(motorista: Motorista, email: String, senha: String): UIstatus<String>

    suspend fun listar(): UIstatus<List<Motorista>>

    suspend fun remover(idMotorista: String): UIstatus<Boolean>

    // Opcional: Útil para o app-motorista verificar o perfil logado
    suspend fun buscarPorId(idMotorista: String): UIstatus<Motorista>
}