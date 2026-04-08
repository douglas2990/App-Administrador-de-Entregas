package com.douglas2990.d2990entregasv2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.d2990entregasv2.domain.usecase.ListarMotoristasUseCase
import com.douglas2990.d2990entregasv2.model.Motorista
import com.example.core.UIstatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListarMotoristasViewModel @Inject constructor(
    private val listarMotoristasUseCase: ListarMotoristasUseCase
) : ViewModel() {

    private val _motoristas = MutableLiveData<UIstatus<List<Motorista>>>()
    val motoristas: LiveData<UIstatus<List<Motorista>>> = _motoristas

    fun carregarMotoristas() {
        _motoristas.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = listarMotoristasUseCase()
            _motoristas.value = resultado
        }
    }
}