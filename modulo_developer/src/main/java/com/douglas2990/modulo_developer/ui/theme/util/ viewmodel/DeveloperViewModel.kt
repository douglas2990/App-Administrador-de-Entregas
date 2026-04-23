package com.douglas2990.modulo_developer.ui.theme.util.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.modulo_developer.model.SolicitacaoAcesso
import com.example.core.UIstatus
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class DeveloperViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _solicitacoes = MutableStateFlow<UIstatus<List<SolicitacaoAcesso>>>(UIstatus.Carregando)
    val solicitacoes = _solicitacoes.asStateFlow()

    init {
        observarSolicitacoes()
    }

    private fun observarSolicitacoes() {
        // SnapshotListener mantém a lista atualizada em tempo real sem refresh manual
        firestore.collection("solicitacoes")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _solicitacoes.value = UIstatus.Erro(error.localizedMessage ?: "Erro desconhecido")
                    return@addSnapshotListener
                }

                val lista = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(SolicitacaoAcesso::class.java)
                } ?: emptyList()

                _solicitacoes.value = UIstatus.Sucesso(lista)
            }
    }

    fun aprovarAcesso(solicitacao: SolicitacaoAcesso) {
        viewModelScope.launch {
            try {
                // 1. Move para autorizados
                firestore.collection("autorizados")
                    .document(solicitacao.email)
                    .set(mapOf("email" to solicitacao.email, "status" to "ATIVO"))
                    .await()

                // 2. Remove das solicitações
                firestore.collection("solicitacoes")
                    .document(solicitacao.email)
                    .delete()
                    .await()
            } catch (e: Exception) {
                // Tratar erro de rede, etc.
            }
        }
    }

    fun rejeitarAcesso(email: String) {
        viewModelScope.launch {
            firestore.collection("solicitacoes").document(email).delete().await()
        }
    }
}