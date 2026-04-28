package com.douglas2990.app_motorista.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.app_motorista.data.repository.model.AgendaDia
import com.example.core.UIstatus
import com.example.core.model.Rota
import com.example.core.usecase.RotaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoricoViewModel @Inject constructor(
    private val rotaUseCase: RotaUseCase
) : ViewModel() {

    private val _statusHistoricoAgenda = MutableLiveData<UIstatus<List<AgendaDia>>>()
    val statusHistoricoAgenda: LiveData<UIstatus<List<AgendaDia>>> = _statusHistoricoAgenda

    fun carregarHistoricoAgrupado(idMotorista: String) {
        _statusHistoricoAgenda.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = rotaUseCase.listarRotasMotorista(idMotorista)
            if (resultado is UIstatus.Sucesso) {
                // Filtramos apenas o que JÁ FOI FINALIZADO
                val rotasFinalizadas = resultado.dados.filter { it.status != "PENDENTE" }
                val agrupado = agruparPorData(rotasFinalizadas)
                _statusHistoricoAgenda.value = UIstatus.Sucesso(agrupado)
            } else if (resultado is UIstatus.Erro) {
                _statusHistoricoAgenda.value = UIstatus.Erro(resultado.erro)
            }
        }
    }

    private fun agruparPorData(rotas: List<Rota>): List<AgendaDia> {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }

        return rotas.groupBy { it.dataPrevistaFormatada.ifEmpty { sdf.format(
            Date(
                it.dataPrevista ?: it.dataCriacao
            )
        ) } }
            .map { (data, lista) ->
                AgendaDia(
                    data = data,
                    rotulo = data, // No histórico, o rótulo é a própria data
                    quantidadeRotas = lista.size,
                    idMotorista = lista.firstOrNull()?.idMotorista ?: ""
                )
            }.sortedByDescending { it.data } // Mais recentes no topo
    }
}