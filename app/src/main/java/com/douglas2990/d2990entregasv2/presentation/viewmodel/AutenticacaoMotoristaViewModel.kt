package com.douglas2990.d2990entregasv2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IEmpresaRepository
import com.douglas2990.d2990entregasv2.domain.usecase.SalvarMotoristaUseCase
import com.douglas2990.d2990entregasv2.model.Empresa
import com.douglas2990.d2990entregasv2.model.Motorista
import com.example.core.UIstatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutenticacaoMotoristaViewModel @Inject constructor(
    private val salvarMotoristaUseCase: SalvarMotoristaUseCase,
    private val empresaRepository: IEmpresaRepository
) : ViewModel() {

    private val _statusCadastro = MutableLiveData<UIstatus<String>>()
    val statusCadastro: LiveData<UIstatus<String>> = _statusCadastro

    private val _statusEmpresas = MutableLiveData<UIstatus<List<Empresa>>>()
    val statusEmpresas: LiveData<UIstatus<List<Empresa>>> = _statusEmpresas

    /**
     * Adaptado para EmpresaRepositoryImpl que usa Callback
     */
    fun carregarEmpresasParaSpinner() {
        _statusEmpresas.value = UIstatus.Carregando

        viewModelScope.launch {
            // Chamamos a função e passamos um bloco { } que
            // será executado quando o Firebase responder
            empresaRepository.listar { resultado ->
                _statusEmpresas.value = resultado
            }
        }
    }

    /**
     * O UseCase geralmente é suspend e retorna o valor diretamente,
     * então este mantemos com atribuição direta.
     */
    fun cadastrarMotorista(motorista: Motorista) {
        _statusCadastro.value = UIstatus.Carregando
        viewModelScope.launch {
            val resultado = salvarMotoristaUseCase(motorista)
            _statusCadastro.value = resultado
        }
    }
}