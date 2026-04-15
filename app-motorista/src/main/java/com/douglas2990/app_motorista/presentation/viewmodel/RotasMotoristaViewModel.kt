package com.douglas2990.app_motorista.presentation.viewmodel


import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.core.UIstatus
import com.example.core.model.Motorista
import com.example.core.model.Rota
import com.example.core.repository.IRotaRepository
import com.example.core.util.PdfRelatorioHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RotasMotoristaViewModel @Inject constructor(
    private val rotaRepository: IRotaRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _rotas = MutableLiveData<UIstatus<List<Rota>>>()
    val rotas: LiveData<UIstatus<List<Rota>>> = _rotas

    private val _nomeEmpresa = MutableLiveData<String>()
    val nomeEmpresa: LiveData<String> = _nomeEmpresa

    private val _pdfArquivoPronto = MutableLiveData<File?>()
    val pdfArquivoPronto: LiveData<File?> = _pdfArquivoPronto

    private val _arquivoPdfGerado = MutableLiveData<File?>()
    val arquivoPdfGerado: LiveData<File?> = _arquivoPdfGerado


    /**
     * Inicia a observação em tempo real.
     * Toda alteração no Firestore refletirá aqui automaticamente.
     */

    fun gerarRelatorio(context: Context, lista: List<Rota>) {
        viewModelScope.launch {
            val empresa = _nomeEmpresa.value ?: "D2990 Entregas"
            val helper = PdfRelatorioHelper(context)
            val file = helper.gerarRelatorio(lista, empresa)
            _arquivoPdfGerado.postValue(file)
        }
    }

    fun prepararRelatorio(context: Context, lista: List<Rota>) {
        viewModelScope.launch {
            val empresa = _nomeEmpresa.value ?: "D2990 Entregas"
            val helper = PdfRelatorioHelper(context)
            val arquivo = helper.gerarRelatorio(lista, empresa)
            _pdfArquivoPronto.postValue(arquivo)
        }
    }
    fun observarMinhasRotas(dataSelecionada: String) {
        val uidMotorista = auth.currentUser?.uid ?: return

        _rotas.value = UIstatus.Carregando

        // Precisamos que seu Repository aceite esse novo parâmetro de data
        rotaRepository.listarPorMotoristaEDataRealTime(uidMotorista, dataSelecionada) { resultado ->
            _rotas.postValue(resultado)
        }
    }

    fun carregarDadosMotorista() {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            // Busca o motorista no repositório (que agora usa a constante certa)
            val motorista = rotaRepository.buscarMotorista(uid)

            if (motorista != null && motorista.nomeEmpresa.isNotEmpty()) {
                _nomeEmpresa.postValue(motorista.nomeEmpresa)
            } else {
                _nomeEmpresa.postValue("Empresa não identificada")
            }
        }
    }

    /**
     * Mantemos este método caso você ainda queira disparar
     * um refresh manual pelo SwipeRefreshLayout.
     */
    /*fun atualizarManualmente() {
        observarMinhasRotas()
    }*/


}