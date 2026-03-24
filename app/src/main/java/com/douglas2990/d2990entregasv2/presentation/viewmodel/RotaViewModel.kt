package com.douglas2990.d2990entregasv2.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.d2990entregasv2.domain.usecase.RotaUseCase
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

    private val _statusUpload = MutableLiveData<UIstatus<String>>()
    val statusUpload: LiveData<UIstatus<String>> = _statusUpload

    private val _statusExclusao = MutableLiveData<UIstatus<Boolean>>()
    val statusExclusao: LiveData<UIstatus<Boolean>> = _statusExclusao

    fun salvarRota(rota: Rota) {
        _statusSalvar.value = UIstatus.Carregando
        viewModelScope.launch {
            // O UseCase agora deve retornar UIstatus.Sucesso(idGerado) ou UIstatus.Erro(mensagem)
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

    fun reportarProblema(idRota: String, motivo: String) {
        // Para reportar problema, geralmente não precisamos de um LiveData de status complexo,
        // apenas atualizar a lista após o sucesso.
        viewModelScope.launch {
            rotaUseCase.reportarProblema(idRota, motivo)
            listarTodasAsRotas()
        }
    }

    fun excluirRota(idRota: String) {
        _statusExclusao.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = rotaUseCase.excluirRota(idRota)
            _statusExclusao.value = resultado
        }
    }
}