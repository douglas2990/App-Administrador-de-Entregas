package com.douglas2990.modulo_developer.ui.theme.util.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.douglas2990.modulo_developer.model.SolicitacaoAcesso
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel

import androidx.compose.runtime.State
import javax.inject.Inject


@HiltViewModel
class SolicitacoesViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    // Lista que o Compose vai "observar"
    private val _listaSolicitacoes = mutableStateOf<List<SolicitacaoAcesso>>(emptyList())
    val listaSolicitacoes: State<List<SolicitacaoAcesso>> = _listaSolicitacoes

    init {
        ouvirSolicitacoes()
    }

    private fun ouvirSolicitacoes() {
        firestore.collection("solicitacoes")
            .whereEqualTo("status", "PENDENTE")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    _listaSolicitacoes.value = snapshot.toObjects(SolicitacaoAcesso::class.java)
                }
            }
    }

    fun aprovar(solicitacao: SolicitacaoAcesso) {
        // 1. Adiciona na Whitelist (autorizados)
        firestore.collection("autorizados")
            .document(solicitacao.email)
            .set(mapOf("email" to solicitacao.email))

        // 2. Muda o status para sumir da lista de pendentes
        firestore.collection("solicitacoes")
            .document(solicitacao.email)
            .update("status", "APROVADO")
    }

    fun rejeitar(solicitacao: SolicitacaoAcesso) {
        firestore.collection("solicitacoes")
            .document(solicitacao.email)
            .update("status", "REJEITADO")
    }
}