package com.example.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SolicitacaoAcesso(
    val id: String = "",           // Geralmente usamos o e-mail como ID
    val nome: String = "",
    val email: String = "",
    val dataSolicitacao: Long = System.currentTimeMillis(),
    val status: String = "PENDENTE",// PENDENTE, APROVADO, REJEITADO
    val stability: Int = 0
) : Parcelable