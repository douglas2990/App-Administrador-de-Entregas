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
            _statusSalvar.value = rotaUseCase.salvarRota(rota)
        }
    }

    fun listarRotasMotorista(idMotorista: String) {
        _rotas.value = UIstatus.Carregando
        viewModelScope.launch {
            _rotas.value = rotaUseCase.listarRotasMotorista(idMotorista)
        }
    }

    fun listarTodasAsRotas() {
        _rotas.value = UIstatus.Carregando
        viewModelScope.launch {
            _rotas.value = rotaUseCase.listarTodasAsRotas()
        }
    }

    fun concluirRota(idRota: String, imageUri: Uri) {
        _statusUpload.value = UIstatus.Carregando
        viewModelScope.launch {
            _statusUpload.value = rotaUseCase.concluirRota(idRota, imageUri)
        }
    }

    fun reportarProblema(idRota: String, motivo: String) {
        viewModelScope.launch {
            rotaUseCase.reportarProblema(idRota, motivo)
            listarTodasAsRotas() 
        }
    }

    fun excluirRota(idRota: String) {
        _statusExclusao.value = UIstatus.Carregando
        viewModelScope.launch {
            _statusExclusao.value = rotaUseCase.excluirRota(idRota)
        }
    }
}
