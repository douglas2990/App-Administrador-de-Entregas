package com.douglas2990.d2990entregasv2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.ICadastroAcessoRepository
import com.example.core.UIstatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CadastroAcessoViewModel @Inject constructor(
    private val repository: ICadastroAcessoRepository
) : ViewModel() {

    private val _status = MutableStateFlow<UIstatus<String>?>(null)
    val status = _status.asStateFlow()

    fun cadastrarNovoMotorista(email: String, senha: String, nome: String) {
        // Validação simples antes de chamar o repositório
        if (email.isBlank() || senha.length < 6 || nome.isBlank()) {
            _status.value = UIstatus.Erro("Preencha todos os campos. A senha deve ter 6+ caracteres.")
            return
        }

        viewModelScope.launch {
            _status.value = UIstatus.Carregando
            val resultado = repository.criarAcessoMotorista(email, senha, nome)
            _status.value = resultado
        }
    }

    fun resetarStatus() {
        _status.value = null
    }
}