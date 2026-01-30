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
class CadastroEmpresaViewModel @Inject constructor(
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

    fun isCnpjValido(cnpj: String): Boolean {
        val numeros = cnpj.replace(Regex("[^\\d]"), "") // Garante que só temos números
        if (numeros.length != 14) return false

        // Lista de CNPJs inválidos conhecidos (todos números iguais)
        val invalidos = listOf("00000000000000", "11111111111111", "22222222222222",
            "33333333333333", "44444444444444", "55555555555555",
            "66666666666666", "77777777777777", "88888888888888", "99999999999999")
        if (invalidos.contains(numeros)) return false

        // Cálculo simplificado dos dígitos verificadores (Regra da Receita Federal)
        return try {
            val calcDigit = { str: String, weights: IntArray ->
                var sum = 0
                for (i in str.indices) sum += (str[i] - '0') * weights[i]
                val rem = sum % 11
                if (rem < 2) '0' else (11 - rem).toString()[0]
            }

            val w1 = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
            val w2 = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

            val d1 = calcDigit(numeros.substring(0, 12), w1)
            val d2 = calcDigit(numeros.substring(0, 12) + d1, w2)

            numeros.endsWith("$d1$d2")
        } catch (e: Exception) {
            false
        }
    }


}