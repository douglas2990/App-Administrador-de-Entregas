package com.douglas2990.app_motorista.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.core.UIstatus
import com.example.core.repository.IRotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalhesEntregaViewModel @Inject constructor(
    private val rotaRepository: IRotaRepository
) : ViewModel() {

    private val _statusEntrega = MutableLiveData<UIstatus<String>>()
    val statusEntrega: LiveData<UIstatus<String>> = _statusEntrega

    fun finalizarEntrega(idRota: String, imageUri: Uri) {
        _statusEntrega.value = UIstatus.Carregando
        viewModelScope.launch {
            // Aqui enviamos o status "CONCLUIDA" e a foto
            val resultado = rotaRepository.finalizarRotaComSucesso(idRota, imageUri)
            _statusEntrega.value = resultado
        }
    }

    fun reportarProblema(idRota: String, motivo: String, imageUri: Uri? = null) {
        _statusEntrega.value = UIstatus.Carregando
        viewModelScope.launch {
            // Note que passamos o status "PROBLEMA", o motivo para o campo 'observacao'
            // e a imageUri que pode ser null
            val resultado = rotaRepository.reportarProblemaRota(idRota, motivo, imageUri)

            if (resultado is UIstatus.Sucesso) {
                _statusEntrega.value = UIstatus.Sucesso("Problema reportado com sucesso")
            } else if (resultado is UIstatus.Erro) {
                _statusEntrega.value = UIstatus.Erro(resultado.erro)
            }
        }
    }
}
