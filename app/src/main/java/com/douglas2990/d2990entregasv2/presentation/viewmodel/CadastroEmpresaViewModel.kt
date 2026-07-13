package com.douglas2990.d2990entregasv2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IEmpresaRepository
import com.douglas2990.d2990entregasv2.domain.usecase.ResultadoValidacao
import com.douglas2990.d2990entregasv2.model.Empresa
import com.example.core.UIstatus
import com.example.core.util.ValidadorCnpj
import com.example.core.util.ValidadorEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CadastroEmpresaViewModel @Inject constructor(
    private val empresaRepository: IEmpresaRepository
): ViewModel() {

    private val _resultadoValidacao = MutableLiveData<ResultadoValidacao>()
    val resultadoValidacao: LiveData<ResultadoValidacao>
        get() = _resultadoValidacao

    private val _uiStatus = MutableLiveData<UIstatus<Any>>()
    val uiStatus: LiveData<UIstatus<Any>> = _uiStatus


    fun remover(
        idProduto: String,
        uiStatus: (UIstatus<Boolean>)->Unit
    ){
        uiStatus.invoke( UIstatus.Carregando )
        viewModelScope.launch {
            empresaRepository.remover( idProduto, uiStatus )
        }
    }

    fun recuperarEmpresaPeloId(
        idEmpresa: String,
        uiStatus: (UIstatus<Empresa> )->Unit
    ){
        uiStatus.invoke( UIstatus.Carregando )
        viewModelScope.launch {
            empresaRepository.recuperarEmpresaPeloId( idEmpresa, uiStatus )
        }
    }

    fun listar( uiStatus: (UIstatus<List<Empresa>> )->Unit ){
        uiStatus.invoke( UIstatus.Carregando )
        viewModelScope.launch {
            empresaRepository.listar( uiStatus )
        }
    }

    fun listar2(){
        _uiStatus.value = UIstatus.Carregando
        viewModelScope.launch {
            // O Repository ainda pede o callback, então passamos o resultado para o LiveData
            empresaRepository.listar { resultado ->
                _uiStatus.value = resultado
            }
        }
    }



    fun salvarCadastroEmpresa(
        empresa: Empresa,
        uiStatus: (UIstatus<String>)->Unit
    ){
        uiStatus.invoke( UIstatus.Carregando )
        viewModelScope.launch {
            if( empresa.id.isEmpty() ){//salvar
                empresaRepository.salvar( empresa, uiStatus )
            }else{//atualizar
                empresaRepository.atualizar( empresa, uiStatus )
            }
        }
    }

    fun salvarCadastroEmpresa2(
        empresa: Empresa
    ) {

        // 1. Validação usando Domain Helpers (Puro Kotlin)
        if (!ValidadorCnpj.isValido(empresa.cnpj)) {
            _uiStatus.value = UIstatus.Erro("CNPJ inválido")
            return
        }

        if (!ValidadorEmail.isValido(empresa.email)) {
            _uiStatus.value = UIstatus.Erro("E-mail inválido")
            return
        }

        _uiStatus.value = UIstatus.Carregando
        viewModelScope.launch {
            if (empresa.id.isEmpty()) {
                empresaRepository.salvar(empresa) { resultado ->
                    _uiStatus.value = resultado
                }
            } else {
                empresaRepository.atualizar(empresa) { resultado ->
                    _uiStatus.value = resultado
                }
            }
        }
    }




}