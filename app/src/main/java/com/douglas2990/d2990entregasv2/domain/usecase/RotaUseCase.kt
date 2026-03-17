package com.douglas2990.d2990entregasv2.domain.usecase

import android.net.Uri
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IRotaRepository
import com.douglas2990.d2990entregasv2.model.Rota
import com.example.core.UIstatus
import javax.inject.Inject

class RotaUseCase @Inject constructor(
    private val repository: IRotaRepository
) {
    suspend fun salvarRota(rota: Rota): UIstatus<String> {
        if (rota.os.isEmpty()) return UIstatus.Erro("O número da OS é obrigatório")
        if (rota.endereco.isEmpty()) return UIstatus.Erro("O endereço é obrigatório")
        if (rota.idMotorista.isEmpty()) return UIstatus.Erro("Selecione um motorista")
        
        return repository.salvar(rota)
    }

    suspend fun listarRotasMotorista(idMotorista: String) = repository.listarPorMotorista(idMotorista)

    suspend fun listarTodasAsRotas() = repository.listarTodas()

    suspend fun concluirRota(idRota: String, imageUri: Uri) = repository.enviarComprovante(idRota, imageUri)

    suspend fun reportarProblema(idRota: String, motivo: String) = 
        repository.atualizarStatus(idRota, "PROBLEMA", motivo)

    suspend fun excluirRota(idRota: String) = repository.remover(idRota)
}
