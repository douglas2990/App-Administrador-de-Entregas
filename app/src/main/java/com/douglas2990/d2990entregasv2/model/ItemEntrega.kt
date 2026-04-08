package com.douglas2990.d2990entregasv2.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemEntrega(
    val id: String = "",
    val numeroNota: String = "",
    val cliente: String = "",
    val endereco: String = "",
    val bairro: String = "",
    val cidade: String = "Diadema", // Valor padrão baseado na sua localização atual
    val status: String = "PENDENTE", // PENDENTE, ENTREGUE, PROBLEMA
    val observacao: String = "",
    val urlFotoComprovante: String = "", // Para o upload que o motorista faz
    val dataHoraEntrega: Long? = null
) : Parcelable