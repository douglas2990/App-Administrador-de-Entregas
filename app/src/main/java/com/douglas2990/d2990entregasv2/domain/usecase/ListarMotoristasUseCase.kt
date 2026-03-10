package com.douglas2990.d2990entregasv2.domain.usecase

import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IMotoristaRepository
import com.douglas2990.d2990entregasv2.model.Motorista
import com.example.core.UIstatus
import javax.inject.Inject

class ListarMotoristasUseCase @Inject constructor(
    private val repository: IMotoristaRepository
) {
    suspend operator fun invoke(): UIstatus<List<Motorista>> {
        return try {
            repository.listar()
        } catch (e: Exception) {
            UIstatus.Erro("Falha ao processar lista: ${e.message}")
        }
    }
}