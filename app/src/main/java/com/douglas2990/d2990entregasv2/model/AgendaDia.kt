package com.douglas2990.d2990entregasv2.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AgendaDia(
    val data: String,
    val rotulo: String,
    val quantidadeRotas: Int,
    val idMotorista: String = ""
) : Parcelable