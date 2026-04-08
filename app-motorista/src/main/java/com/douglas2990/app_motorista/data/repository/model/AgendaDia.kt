package com.douglas2990.app_motorista.data.repository.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AgendaDia(
    val data: String,           // Ex: "07/04/2026"
    val rotulo: String,         // Ex: "Hoje" ou "Amanhã"
    val quantidadeRotas: Int,    // Ex: 3
    val idMotorista: String = "" // Útil para filtrar as rotas na próxima tela
) : Parcelable