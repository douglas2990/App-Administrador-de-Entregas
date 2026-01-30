package com.douglas2990.d2990entregasv2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IEmpresaRepository
import com.douglas2990.d2990entregasv2.domain.usecase.ResultadoValidacao
import com.douglas2990.d2990entregasv2.model.Empresa
import com.example.core.UIstatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EmpresaViewModel @Inject constructor(
    private val empresaRepository: IEmpresaRepository
): ViewModel() {

    private val _resultadoValidacao = MutableLiveData<ResultadoValidacao>()
    val resultadoValidacao: LiveData<ResultadoValidacao>
        get() = _resultadoValidacao


    fun remover(
        idProduto: String,
        uiStatus: (UIstatus<Boolean>)->Unit
    ){
        uiStatus.invoke( UIstatus.Carregando )
        viewModelScope.launch {
            empresaRepository.remover( idProduto, uiStatus )
        }
    }

    fun recuperarProdutoPeloId(
        idProduto: String,
        uiStatus: (UIstatus<Empresa> )->Unit
    ){
        uiStatus.invoke( UIstatus.Carregando )
        viewModelScope.launch {
            empresaRepository.recuperarEmpresaPeloId( idProduto, uiStatus )
        }
    }

    fun listar( uiStatus: (UIstatus<List<Empresa>> )->Unit ){
        uiStatus.invoke( UIstatus.Carregando )
        viewModelScope.launch {
            empresaRepository.listar( uiStatus )
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


}