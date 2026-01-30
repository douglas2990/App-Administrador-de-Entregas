package com.douglas2990.d2990entregasv2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IAutenticacaoMotoristaRepository
import com.douglas2990.d2990entregasv2.domain.usecase.AutenticacaoMotoristaUseCase
import com.douglas2990.d2990entregasv2.domain.usecase.ResultadoValidacao
import com.douglas2990.d2990entregasv2.model.Motorista
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AutenticacaoMotoristaViewModel @Inject constructor(
    private val autenticacaoMotoristaUseCase: AutenticacaoMotoristaUseCase,
    private val autenticacaoRepositoryImpl: IAutenticacaoMotoristaRepository
): ViewModel() {

    private val _resultadoValidacao = MutableLiveData<ResultadoValidacao>()
    val resultadoValidacao: LiveData<ResultadoValidacao>
        get() = _resultadoValidacao

    private val _sucesso = MutableLiveData<Boolean>()
    val sucesso: LiveData<Boolean>
        get() = _sucesso

    fun cadastrarMotorista(motorista: Motorista){
        val retornoValidacao = autenticacaoMotoristaUseCase.validarCadastroUsuario(motorista)
        _resultadoValidacao.value = retornoValidacao
        if ( retornoValidacao.sucessoValidacaoCadastro ){
            viewModelScope.launch {
                val retorno = autenticacaoRepositoryImpl.cadastrarMotorista( motorista )
                _sucesso.postValue( retorno )

            }
        }
    }

}