package com.douglas2990.d2990entregasv2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IEmpresaRepository
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IMotoristaRepository
import com.douglas2990.d2990entregasv2.model.Empresa
import com.douglas2990.d2990entregasv2.model.Motorista
import com.example.core.UIstatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CadastroMotoristaViewModel @Inject constructor(
    private val empresaRepository: IEmpresaRepository,
    private val motoristaRepository: IMotoristaRepository
) : ViewModel() {

    private val _empresas = MutableLiveData<UIstatus<List<Empresa>>>()
    val empresas: LiveData<UIstatus<List<Empresa>>> = _empresas

    private val _statusCadastro = MutableLiveData<UIstatus<String>>()
    val statusCadastro: LiveData<UIstatus<String>> = _statusCadastro

    // Carrega as empresas do "Guilherme" logado para o Spinner
    fun carregarEmpresas() {
        _empresas.value = UIstatus.Carregando
        viewModelScope.launch {
            empresaRepository.listar { resultado ->
                _empresas.value = resultado
            }
        }
    }

    fun cadastrarMotorista(motorista: Motorista) {
        _statusCadastro.value = UIstatus.Carregando
        viewModelScope.launch {
            // O repositório retorna UIstatus diretamente agora (sem callback)
            val resultado = motoristaRepository.salvar(motorista)
            _statusCadastro.value = resultado
        }
    }
}