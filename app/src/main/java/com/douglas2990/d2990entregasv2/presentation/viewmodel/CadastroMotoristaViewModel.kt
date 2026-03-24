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

    fun carregarEmpresas() {
        _empresas.value = UIstatus.Carregando
        viewModelScope.launch {
            empresaRepository.listar { resultado ->
                _empresas.value = resultado
            }
        }
    }

    // AGORA RECEBE EMAIL E SENHA DA TELA
    fun cadastrarMotorista(motorista: Motorista, email: String, senha: String) {

        // Validação básica antes de tentar o Firebase
        if (email.isEmpty() || senha.length < 6) {
            _statusCadastro.value = UIstatus.Erro("E-mail inválido ou senha muito curta (mínimo 6 caracteres)")
            return
        }

        _statusCadastro.value = UIstatus.Carregando
        viewModelScope.launch {
            // Chama o novo método do repositório que sincroniza Auth e Firestore
            val resultado = motoristaRepository.salvar(motorista, email, senha)
            _statusCadastro.value = resultado
        }
    }
}