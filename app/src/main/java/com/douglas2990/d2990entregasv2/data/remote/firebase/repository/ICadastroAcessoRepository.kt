package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import com.example.core.UIstatus

interface ICadastroAcessoRepository {
    suspend fun criarAcessoMotorista(email: String, senha: String, nome: String): UIstatus<String>
}