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
class AgendaViewModel @Inject constructor(
    private val rotaUseCase: RotaUseCase
) : ViewModel() {

    private val _statusAgenda = MutableLiveData<UIstatus<List<AgendaDia>>>()
    val statusAgenda: LiveData<UIstatus<List<AgendaDia>>> = _statusAgenda

    fun carregarAgenda(idMotorista: String) {
        _statusAgenda.value = UIstatus.Carregando

        viewModelScope.launch {
            // Buscamos os dados atualizados do UseCase
            val resultado = rotaUseCase.listarRotasMotorista(idMotorista)

            if (resultado is UIstatus.Sucesso) {
                // Filtro mais robusto ignorando Case Sensitivity
                val rotasPendentes = resultado.dados.filter {
                    it.status?.trim()?.uppercase() == "PENDENTE"
                }

                val agendaAgrupada = agruparRotasPorData(rotasPendentes)
                _statusAgenda.value = UIstatus.Sucesso(agendaAgrupada)
            } else if (resultado is UIstatus.Erro) {
                _statusAgenda.value = UIstatus.Erro(resultado.erro)
            }
        }
    }

    private fun agruparRotasPorData(rotas: List<Rota>): List<AgendaDia> {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }

        val hojeTimestamp = System.currentTimeMillis()
        val hojeStr = sdf.format(Date(hojeTimestamp))
        val amanhaStr = sdf.format(Date(hojeTimestamp + 86400000))

        // 1. Ordenamos as rotas brutas por data antes de agrupar
        return rotas.sortedBy { it.dataPrevista ?: it.dataCriacao }
            .groupBy { rota ->
                if (!rota.dataPrevistaFormatada.isNullOrEmpty()) {
                    rota.dataPrevistaFormatada
                } else {
                    val dataLong = rota.dataPrevista ?: rota.dataCriacao
                    sdf.format(Date(dataLong))
                }
            }.map { (dataString, listaDeRotas) ->
                val rotulo = when (dataString) {
                    hojeStr -> "Hoje"
                    amanhaStr -> "Amanhã"
                    else -> dataString
                }

                AgendaDia(
                    data = dataString,
                    rotulo = rotulo,
                    quantidadeRotas = listaDeRotas.size,
                    idMotorista = listaDeRotas.firstOrNull()?.idMotorista ?: ""
                )
            }
        // A lista final já virá ordenada cronologicamente
    }
}