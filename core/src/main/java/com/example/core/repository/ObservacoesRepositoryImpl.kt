package com.example.core.repository

import com.example.core.UIstatus
import com.example.core.model.ObservacaoCliente
import com.example.core.util.ConstantesFirebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ObservacoesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : IObservacoesRepository {

    private val colecao = firestore.collection("observacoes_clientes")

    override suspend fun salvar(observacao: ObservacaoCliente): UIstatus<String> {
        return try {
            val docRef = if (observacao.id.isEmpty()) {
                colecao.document()
            } else {
                colecao.document(observacao.id)
            }
            
            val observacaoComId = observacao.copy(id = docRef.id)
            docRef.set(observacaoComId).await()
            UIstatus.Sucesso("Observação salva com sucesso!")
        } catch (e: Exception) {
            UIstatus.Erro(e.message ?: "Erro ao salvar observação")
        }
    }

    override suspend fun listarTodas(): UIstatus<List<ObservacaoCliente>> {
        return try {
            val snapshot = colecao
                .orderBy("dataCriacao", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val lista = snapshot.toObjects(ObservacaoCliente::class.java)
            UIstatus.Sucesso(lista)
        } catch (e: Exception) {
            UIstatus.Erro(e.message ?: "Erro ao listar observações")
        }
    }
}
