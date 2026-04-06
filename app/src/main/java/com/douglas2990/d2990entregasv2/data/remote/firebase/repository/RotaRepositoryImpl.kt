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
            val idLogado = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Usuário não autenticado")

            // Se a rota não tem ID, cria um novo. Se tem (edição), usa o existente.
            val refRota = if (rota.id.isEmpty()) colecaoRotas.document() else colecaoRotas.document(rota.id)

            // O idGestor deve ser sempre o UID de quem criou (o Guilherme)
            // Se for uma edição, mantemos o idGestor original da rota
            val idGestorFinal = if (rota.idGestor.isEmpty()) idLogado else rota.idGestor

            val dataFinal = if (rota.id.isEmpty()) System.currentTimeMillis() else rota.dataCriacao

            val rotaFinal = rota.copy(
                id = refRota.id,
                idGestor = idGestorFinal,
                dataCriacao = dataFinal
            )

            refRota.set(rotaFinal).await()
            UIstatus.Sucesso(refRota.id)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao salvar rota: ${e.message}")
        }
    }

    private suspend fun fazerUploadFoto(idRota: String, imageUri: Uri): String {
        val storageRef = firebaseStorage.reference
            .child(ConstantesFirebase.STORAGE_COMPROVANTES)
            .child(idRota)
            .child("comprovante_${System.currentTimeMillis()}.jpg")

        storageRef.putFile(imageUri).await()
        return storageRef.downloadUrl.await().toString()
    }

    override suspend fun listarPorMotorista(idMotorista: String): UIstatus<List<Rota>> {
        return try {
            val userLogado = firebaseAuth.currentUser ?: return UIstatus.Erro("Usuário não autenticado")
            val idLogado = userLogado.uid

            // CRIAMOS A QUERY BASE
            var query = colecaoRotas.whereEqualTo("idMotorista", idMotorista)

            // O PULO DO GATO:
            // Se quem está logado NÃO é o motorista que estamos buscando,
            // significa que é o ADMIN (Guilherme) tentando ver.
            // Então, precisamos filtrar também pelo idGestor dele para a Regra liberar!
            if (idLogado != idMotorista) {
                query = query.whereEqualTo("idGestor", idLogado)
            }

            val querySnapshot = query
                .orderBy("dataCriacao", Query.Direction.DESCENDING)
                .get()
                .await()

            val lista = querySnapshot.toObjects(Rota::class.java)
            UIstatus.Sucesso(lista)
        } catch (e: Exception) {
            // Fallback simples caso dê erro de índice por causa do orderBy
            UIstatus.Erro("Erro ao carregar: ${e.message}")
        }
    }

    override suspend fun listarTodas(): UIstatus<List<Rota>> {
        return try {
            val user = firebaseAuth.currentUser
            val uidAtual = user?.uid

            if (uidAtual == null) {
                return UIstatus.Erro("Usuário deslogado no Firebase!")
            }

            // LOG DE AUDITORIA: Verifique se esse ID no Logcat é o mesmo que está no campo idGestor do banco
            android.util.Log.d("DEBUG_PERMISSAO", "ID Logado no App: $uidAtual")

            val querySnapshot = colecaoRotas
                .whereEqualTo("idGestor", uidAtual)
                .get()
                .await()

            val lista = querySnapshot.toObjects(Rota::class.java)
            UIstatus.Sucesso(lista)

        } catch (e: com.google.firebase.firestore.FirebaseFirestoreException) {
            android.util.Log.e("DEBUG_PERMISSAO", "Erro Código: ${e.code}")
            android.util.Log.e("DEBUG_PERMISSAO", "Erro Mensagem: ${e.message}")
            UIstatus.Erro("Erro de Permissão: ${e.code}")
        } catch (e: Exception) {
            UIstatus.Erro("Erro desconhecido: ${e.message}")
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
        return finalizarRotaComSucesso(idRota, imageUri) // Redireciona para o novo método
    }

    override suspend fun remover(idRota: String): UIstatus<Boolean> {
        return try {
            colecaoRotas.document(idRota).delete().await()
            UIstatus.Sucesso(true)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao remover rota: ${e.message}")
        }
    }

    override suspend fun finalizarRotaComSucesso(idRota: String, imageUri: Uri): UIstatus<String> {
        return try {
            val urlFoto = fazerUploadFoto(idRota, imageUri)

            colecaoRotas.document(idRota).update(
                mapOf(
                    "comprovanteUrl" to urlFoto,
                    "status" to "CONCLUIDA",
                    "observacao" to null // Limpa qualquer observação anterior
                )
            ).await()

            UIstatus.Sucesso("Entrega finalizada com sucesso!")
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao finalizar: ${e.message}")
        }
    }

    override suspend fun reportarProblemaRota(idRota: String, motivo: String, imageUri: Uri?): UIstatus<String> {
        return try {
            val updates = mutableMapOf<String, Any?>(
                "status" to "PROBLEMA",
                "observacao" to motivo
            )

            // Se o motorista tirou foto do problema, fazemos o upload
            imageUri?.let { uri ->
                val urlFoto = fazerUploadFoto(idRota, uri)
                updates["comprovanteUrl"] = urlFoto
            }

            colecaoRotas.document(idRota).update(updates).await()
            UIstatus.Sucesso("Problema reportado com sucesso!")
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao reportar: ${e.message}")
        }
    }
}