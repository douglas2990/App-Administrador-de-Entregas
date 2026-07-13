package com.douglas2990.d2990entregasv2.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.d2990entregasv2.domain.usecase.RotaUseCase
import com.douglas2990.d2990entregasv2.model.ItemAgendaAdmin
import com.douglas2990.d2990entregasv2.model.Rota
import com.example.core.UIstatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RotaViewModel @Inject constructor(
    private val rotaUseCase: RotaUseCase
) : ViewModel() {

    private val _statusSalvar = MutableLiveData<UIstatus<String>>()
    val statusSalvar: LiveData<UIstatus<String>> = _statusSalvar

    private val _rotas = MutableLiveData<UIstatus<List<Rota>>>()
    val rotas: LiveData<UIstatus<List<Rota>>> = _rotas

    private val _datasComRotas = MutableLiveData<UIstatus<List<Long>>>()
    val datasComRotas: LiveData<UIstatus<List<Long>>> = _datasComRotas

    private val _statusUpload = MutableLiveData<UIstatus<String>>()
    val statusUpload: LiveData<UIstatus<String>> = _statusUpload

    private val _statusExclusao = MutableLiveData<UIstatus<Boolean>>()
    val statusExclusao: LiveData<UIstatus<Boolean>> = _statusExclusao

    private val _datasStatus = MutableLiveData<UIstatus<List<ItemAgendaAdmin>>>()
    val datasStatus: LiveData<UIstatus<List<ItemAgendaAdmin>>> = _datasStatus

    private val _rotasArquivadas = MutableLiveData<UIstatus<List<Rota>>>()
    val rotasArquivadas: LiveData<UIstatus<List<Rota>>> = _rotasArquivadas

    private val _datasArquivadas = MutableLiveData<UIstatus<List<ItemAgendaAdmin>>>()
    val datasArquivadas: LiveData<UIstatus<List<ItemAgendaAdmin>>> = _datasArquivadas

    private val _agendaAtiva = MutableLiveData<UIstatus<List<ItemAgendaAdmin>>>()
    val agendaAtiva: LiveData<UIstatus<List<ItemAgendaAdmin>>> = _agendaAtiva

    private val _agendaArquivada = MutableLiveData<UIstatus<List<ItemAgendaAdmin>>>()
    val agendaArquivada: LiveData<UIstatus<List<ItemAgendaAdmin>>> = _agendaArquivada

    private val _statusArquivamento = MutableLiveData<UIstatus<Boolean>>()
    val statusArquivamento: LiveData<UIstatus<Boolean>> = _statusArquivamento

    // 📝 Salvar serve tanto para Criar quanto para EDITAR (Se a rota já tiver um ID)
    fun salvarRota(rota: Rota) {
        _statusSalvar.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = rotaUseCase.salvarRota(rota)
            _statusSalvar.value = resultado
        }
    }

    fun listarRotasMotorista(idMotorista: String) {
        _rotas.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = rotaUseCase.listarRotasMotorista(idMotorista)
            _rotas.value = resultado
        }
    }

    fun listarTodasAsRotas() {
        _rotas.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = rotaUseCase.listarTodasAsRotas()
            _rotas.value = resultado
        }
    }

    fun concluirRota(idRota: String, imageUri: Uri) {
        _statusUpload.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = rotaUseCase.concluirRota(idRota, imageUri)
            _statusUpload.value = resultado
        }
    }

    // 🔄 Ajustado: Adicionado idMotorista e data Opcional para atualizar a lista certa após reportar
    fun reportarProblema(idRota: String, motivo: String, idMotorista: String? = null, dataSelecionada: Long = -1L) {
        viewModelScope.launch {
            rotaUseCase.reportarProblema(idRota, motivo)

            // Recarrega de forma inteligente dependendo de onde o Admin está olhando
            idMotorista?.let { id ->
                if (dataSelecionada != -1L) {
                    listarPorDataEMotorista(id, dataSelecionada)
                } else {
                    listarRotasMotorista(id)
                }
            } ?: listarTodasAsRotas()
        }
    }

    // 🗑️ Excluir Rota de forma limpa
    fun excluirRota(idRota: String) {
        _statusExclusao.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = rotaUseCase.excluirRota(idRota)
            _statusExclusao.value = resultado
        }
    }

    fun listarDatasComRotasAdmin(idMotorista: String) {
        _datasComRotas.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = rotaUseCase.listarDatasComRotasAdmin(idMotorista)
            _datasComRotas.value = resultado
        }
    }

    fun listarDatasComRotasMotorista(idMotorista: String) {
        _datasComRotas.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = rotaUseCase.listarDatasComRotas(idMotorista)
            _datasComRotas.value = resultado
        }
    }

    fun listarPorDataEMotorista(idMotorista: String, data: Long) {
        _rotas.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = rotaUseCase.listarPorDataEMotorista(idMotorista, data)
            _rotas.value = resultado
        }
    }

    fun listarDatasComStatusAdmin(idMotorista: String) {
        _datasStatus.value = UIstatus.Carregando
        viewModelScope.launch {
            _datasStatus.value = rotaUseCase.listarDatasComStatusAdmin(idMotorista)
        }
    }

    fun listarDatasArquivadas(idMotorista: String) {
        _datasArquivadas.value = UIstatus.Carregando
        viewModelScope.launch {
            _datasArquivadas.value = rotaUseCase.listarDatasArquivadas(idMotorista)
        }
    }

    fun listarAgendaAtiva(idMotorista: String) {
        _agendaAtiva.value = UIstatus.Carregando
        viewModelScope.launch {
            _agendaAtiva.value = rotaUseCase.listarAgendaAtiva(idMotorista)
        }
    }

    fun listarAgendaArquivada(idMotorista: String) {
        _agendaArquivada.value = UIstatus.Carregando
        viewModelScope.launch {
            _agendaArquivada.value = rotaUseCase.listarAgendaArquivada(idMotorista)
        }
    }

    fun listarRotasArquivadasPorData(idMotorista: String, data: Long) {
        _rotas.value = UIstatus.Carregando
        viewModelScope.launch {
            _rotas.value = rotaUseCase.listarRotasArquivadasPorData(idMotorista, data)
        }
    }

    fun arquivarDia(idMotorista: String, data: Long) {
        _statusArquivamento.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = rotaUseCase.executarArquivamento(idMotorista, data)
            _statusArquivamento.value = resultado

            if (resultado is UIstatus.Sucesso) {
                listarAgendaAtiva(idMotorista)
            }
        }
    }
}