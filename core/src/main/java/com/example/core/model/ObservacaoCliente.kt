package com.example.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ObservacaoCliente(
    val id: String = "",
    val nomeCliente: String = "",
    val endereco: String = "",
    val relato: String = "",
    val idMotorista: String = "",
    val nomeMotorista: String = "",
    val dataCriacao: Long = System.currentTimeMillis()
) : Parcelable
