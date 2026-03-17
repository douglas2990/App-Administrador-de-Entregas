package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import android.net.Uri
import com.douglas2990.d2990entregasv2.model.Rota
import com.example.core.UIstatus
import com.example.core.util.ConstantesFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RotaRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : IRotaRepository {

    private val colecaoRotas = firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_ROTAS)

    override suspend fun salvar(rota: Rota): UIstatus<String> {
        return try {
            val idGestor = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Usuário não autenticado")

            val refRota = if (rota.id.isEmpty()) colecaoRotas.document() else colecaoRotas.document(rota.id)
            val rotaFinal = rota.copy(id = refRota.id, idGestor = idGestor)
            
            refRota.set(rotaFinal).await()
            UIstatus.Sucesso(refRota.id)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao salvar rota: ${e.message}")
        }
    }

    override suspend fun listarPorMotorista(idMotorista: String): UIstatus<List<Rota>> {
        return try {
            val idGestor = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Usuário não autenticado")

            // IMPORTANTE: A Query deve incluir o idGestor para bater com a regra de segurança
            val querySnapshot = colecaoRotas
                .whereEqualTo("idGestor", idGestor)
                .whereEqualTo("idMotorista", idMotorista)
                .orderBy("dataCriacao", Query.Direction.DESCENDING)
                .get()
                .await()

            val lista = querySnapshot.toObjects(Rota::class.java)
            UIstatus.Sucesso(lista)
        } catch (e: Exception) {
            // Fallback caso falte índice ou dê erro de permissão
            try {
                val idGestor = firebaseAuth.currentUser?.uid!!
                val querySnapshotSemOrdem = colecaoRotas
                    .whereEqualTo("idGestor", idGestor)
                    .whereEqualTo("idMotorista", idMotorista)
                    .get()
                    .await()
                val lista = querySnapshotSemOrdem.toObjects(Rota::class.java)
                UIstatus.Sucesso(lista)
            } catch (e2: Exception) {
                UIstatus.Erro("Erro de permissão: Certifique-se de que o idGestor está correto nos documentos.")
            }
        }
    }

    override suspend fun listarTodas(): UIstatus<List<Rota>> {
        return try {
            val idGestor = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Usuário não autenticado")

            val querySnapshot = colecaoRotas
                .whereEqualTo("idGestor", idGestor)
                .orderBy("dataCriacao", Query.Direction.DESCENDING)
                .get()
                .await()

            val lista = querySnapshot.toObjects(Rota::class.java)
            UIstatus.Sucesso(lista)
        } catch (e: Exception) {
            try {
                val idGestor = firebaseAuth.currentUser?.uid!!
                val querySnapshotSemOrdem = colecaoRotas
                    .whereEqualTo("idGestor", idGestor)
                    .get()
                    .await()
                val lista = querySnapshotSemOrdem.toObjects(Rota::class.java)
                UIstatus.Sucesso(lista)
            } catch (e2: Exception) {
                UIstatus.Erro("Erro ao listar rotas gerais: ${e2.message}")
            }
        }
    }

    override suspend fun atualizarStatus(idRota: String, status: String, observacao: String?): UIstatus<Boolean> {
        return try {
            val updates = mutableMapOf<String, Any>("status" to status)
            observacao?.let { updates["observacao"] = it }

            colecaoRotas.document(idRota).update(updates).await()
            UIstatus.Sucesso(true)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao atualizar status: ${e.message}")
        }
    }

    override suspend fun enviarComprovante(idRota: String, imageUri: Uri): UIstatus<String> {
        return try {
            val storageRef = firebaseStorage.reference
                .child(ConstantesFirebase.STORAGE_COMPROVANTES)
                .child(idRota)
                .child("comprovante_${System.currentTimeMillis()}.jpg")

            storageRef.putFile(imageUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            colecaoRotas.document(idRota).update(
                mapOf("comprovanteUrl" to downloadUrl, "status" to "CONCLUIDA")
            ).await()

            UIstatus.Sucesso(downloadUrl)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao enviar comprovante: ${e.message}")
        }
    }

    override suspend fun remover(idRota: String): UIstatus<Boolean> {
        return try {
            colecaoRotas.document(idRota).delete().await()
            UIstatus.Sucesso(true)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao remover rota: ${e.message}")
        }
    }
}
