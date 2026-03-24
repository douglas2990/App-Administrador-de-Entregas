package com.douglas2990.app_motorista.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.app_motorista.data.repository.IAutenticacaoMotoristaRepository
import com.example.core.UIstatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IAutenticacaoMotoristaRepository
) : ViewModel() {

    private val _loginStatus = MutableLiveData<UIstatus<String>>()
    val loginStatus: LiveData<UIstatus<String>> = _loginStatus

    fun login(email: String, senha: String) {
        if (email.isEmpty() || senha.isEmpty()) {
            _loginStatus.value = UIstatus.Erro("Preencha todos os campos")
            return
        }

        _loginStatus.value = UIstatus.Carregando
        viewModelScope.launch {
            _loginStatus.value = repository.login(email, senha)
        }
    }

    fun usuarioLogado() = repository.usuarioLogado()
}
