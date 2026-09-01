package com.douglas2990.app_motorista.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.UIstatus
import com.example.core.model.ObservacaoCliente
import com.example.core.repository.IObservacoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObservacoesViewModel @Inject constructor(
    private val repository: IObservacoesRepository
) : ViewModel() {

    private val _statusSalvar = MutableLiveData<UIstatus<String>?>()
    val statusSalvar: LiveData<UIstatus<String>?> = _statusSalvar

    private val _observacoes = MutableLiveData<UIstatus<List<ObservacaoCliente>>>()
    val observacoes: LiveData<UIstatus<List<ObservacaoCliente>>> = _observacoes

    fun salvarObservacao(nomeCliente: String, endereco: String, relato: String, idMotorista: String, nomeMotorista: String) {
        if (nomeCliente.isBlank() || relato.isBlank()) {
            _statusSalvar.value = UIstatus.Erro("Preencha o nome do cliente e o relato.")
            return
        }

        val observacao = ObservacaoCliente(
            nomeCliente = nomeCliente,
            endereco = endereco,
            relato = relato,
            idMotorista = idMotorista,
            nomeMotorista = nomeMotorista
        )

        _statusSalvar.value = UIstatus.Carregando
        viewModelScope.launch {
            _statusSalvar.value = repository.salvar(observacao)
        }
    }

    fun listarObservacoes() {
        _observacoes.value = UIstatus.Carregando
        viewModelScope.launch {
            _observacoes.value = repository.listarTodas()
        }
    }

    fun resetStatusSalvar() {
        _statusSalvar.value = null
    }
}
