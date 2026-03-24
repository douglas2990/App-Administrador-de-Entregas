package com.example.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rota(
    val id: String = "",
    val os: String = "",
    val nomeEmpresaDestino: String = "",
    val endereco: String = "",
    val idMotorista: String = "",
    val nomeMotorista: String = "",
    val idGestor: String = "",
    val status: String = "PENDENTE", // PENDENTE, CONCLUIDA, PROBLEMA
    val comprovanteUrl: String? = null,
    val observacao: String? = null,
    val dataCriacao: Long = System.currentTimeMillis()
) : Parcelable
