package com.douglas2990.app_motorista.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.UIstatus
import com.example.core.model.Rota
import com.example.core.repository.IRotaRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RotasMotoristaViewModel @Inject constructor(
    private val rotaRepository: IRotaRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _rotas = MutableLiveData<UIstatus<List<Rota>>>()
    val rotas: LiveData<UIstatus<List<Rota>>> = _rotas

    /**
     * Inicia a observação em tempo real.
     * Toda alteração no Firestore refletirá aqui automaticamente.
     */
    fun observarMinhasRotas(dataSelecionada: String) {
        val uidMotorista = auth.currentUser?.uid ?: return

        _rotas.value = UIstatus.Carregando

        // Precisamos que seu Repository aceite esse novo parâmetro de data
        rotaRepository.listarPorMotoristaEDataRealTime(uidMotorista, dataSelecionada) { resultado ->
            _rotas.postValue(resultado)
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