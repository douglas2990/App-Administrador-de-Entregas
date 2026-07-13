package com.douglas2990.modulo_developer.ui.theme.util.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.modulo_developer.model.SolicitacaoAcesso
import com.example.core.UIstatus
import com.example.core.util.ConstantesFirebase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeveloperViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    // Note que removi os () de Carregando e simplifiquei a declaração
    private val _solicitacoes = MutableStateFlow<UIstatus<List<SolicitacaoAcesso>>>(UIstatus.Carregando)
    val solicitacoes: StateFlow<UIstatus<List<SolicitacaoAcesso>>> = _solicitacoes

    init {
        ouvirSolicitacoes()
    }

    private fun ouvirSolicitacoes() {
        firestore.collection("solicitacoes")
            .whereEqualTo("status", "PENDENTE")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _solicitacoes.value = UIstatus.Erro(error.message ?: "Erro desconhecido")
                    return@addSnapshotListener
                }

                val lista = snapshot?.toObjects(SolicitacaoAcesso::class.java) ?: emptyList()
                _solicitacoes.value = UIstatus.Sucesso(lista)
            }
    }

    fun aprovarAcesso(solicitacao: SolicitacaoAcesso) {
        viewModelScope.launch {
            try {
                // 1. Adiciona na Whitelist
                firestore.collection(ConstantesFirebase.FIRESTORE_AUTORIZADOS)
                    .document(solicitacao.email)
                    .set(mapOf("email" to solicitacao.email))

                // 2. Atualiza status para APROVADO
                firestore.collection("solicitacoes")
                    .document(solicitacao.email)
                    .update("status", "APROVADO")
            } catch (e: Exception) {
                _solicitacoes.value = UIstatus.Erro("Falha ao aprovar: ${e.message}")
            }
        }
    }

    fun rejeitarAcesso(solicitacao: SolicitacaoAcesso) {
        viewModelScope.launch {
            try {
                firestore.collection("solicitacoes")
                    .document(solicitacao.email)
                    .update("status", "REJEITADO")
            } catch (e: Exception) {
                _solicitacoes.value = UIstatus.Erro("Falha ao rejeitar: ${e.message}")
            }
        }
    }
}