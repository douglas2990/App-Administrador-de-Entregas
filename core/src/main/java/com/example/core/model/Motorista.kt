package com.example.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Motorista(
    val id: String = "",
    val idGestor: String = "",
    val nome: String = "",
    val email: String = "",
    val senha: String = "", // Primeira senha cadastrada pelo gestor
    val telefone: String = "",
    val idEmpresa: String = "", // Vinculação técnica
    val nomeEmpresa: String = "" // Para exibição facilitada
) : Parcelable