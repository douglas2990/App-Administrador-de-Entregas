package com.example.core.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.core.UIstatus
import com.example.core.model.Motorista
import com.example.core.model.Rota
import com.example.core.util.ConstantesFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class RotaRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    @ApplicationContext private val context: Context
) : IRotaRepository {

    private val colecaoRotas = firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_ROTAS)

    // --- MÉTODOS DE ESCRITA (ADMIN) ---

    override suspend fun salvar(rota: Rota): UIstatus<String> {
        return try {
            val idLogado = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Usuário não autenticado")

            val refRota = if (rota.id.isEmpty()) colecaoRotas.document() else colecaoRotas.document(rota.id)

            val idGestorFinal = if (rota.idGestor.isEmpty()) idLogado else rota.idGestor
            val dataCriacaoFinal = if (rota.id.isEmpty()) System.currentTimeMillis() else rota.dataCriacao

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val dataFormatada = rota.dataPrevista?.let { sdf.format(Date(it)) } ?: ""

            val rotaFinal = rota.copy(
                id = refRota.id,
                idGestor = idGestorFinal,
                dataCriacao = dataCriacaoFinal,
                dataPrevistaFormatada = dataFormatada,
                status = if (rota.id.isEmpty()) "PENDENTE" else rota.status,
                arquivadaAdmin = rota.arquivadaAdmin,
                arquivadaMotorista = rota.arquivadaMotorista,
            )

            refRota.set(rotaFinal).await()
            UIstatus.Sucesso(refRota.id)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao salvar rota: ${e.message}")
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

    // --- MÉTODOS DE LEITURA (FILTROS) ---

    override suspend fun listarTodas(): UIstatus<List<Rota>> {
        return try {
            val uidAtual = firebaseAuth.currentUser?.uid ?: return UIstatus.Erro("Deslogado")

            val querySnapshot = colecaoRotas
                .whereEqualTo(ConstantesFirebase.CAMPO_ID_GESTOR, uidAtual)
                .orderBy(ConstantesFirebase.CAMPO_DATA_PREVISTA, Query.Direction.ASCENDING)
                .get()
                .await()

            val lista = querySnapshot.toObjects(Rota::class.java)
            UIstatus.Sucesso(lista)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao listar rotas: ${e.message}")
        }
    }

    override suspend fun listarPorMotorista(idMotorista: String): UIstatus<List<Rota>> {
        return try {
            val querySnapshot = colecaoRotas
                .whereEqualTo(ConstantesFirebase.CAMPO_ID_MOTORISTA, idMotorista)
                .orderBy("dataCriacao", Query.Direction.DESCENDING)
                .get()
                .await()

            val lista = querySnapshot.toObjects(Rota::class.java)
            UIstatus.Sucesso(lista)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao carregar rotas: ${e.message}")
        }
    }

    // --- MÉTODOS DO CALENDÁRIO (MOTORISTA) ---

    override suspend fun listarDatasComRotas(idMotorista: String): UIstatus<List<Long>> {
        return try {
            val querySnapshot = colecaoRotas
                .whereEqualTo(ConstantesFirebase.CAMPO_ID_MOTORISTA, idMotorista)
                .whereIn(ConstantesFirebase.CAMPO_STATUS, listOf("PENDENTE", "PROBLEMA"))
                .get()
                .await()

            val rotas = querySnapshot.toObjects(Rota::class.java)

            // Filtro de autonomia do motorista usando constante
            val datasUnicas = rotas.filter { it.arquivadaMotorista == false }
                .mapNotNull { it.dataPrevista }
                .distinct()
                .sorted()

            UIstatus.Sucesso(datasUnicas)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao buscar datas: ${e.message}")
        }
    }

    override suspend fun listarPorDataEMotorista(idMotorista: String, data: Long): UIstatus<List<Rota>> {
        return try {
            val querySnapshot = colecaoRotas
                .whereEqualTo(ConstantesFirebase.CAMPO_ID_MOTORISTA, idMotorista)
                .whereEqualTo(ConstantesFirebase.CAMPO_DATA_PREVISTA, data)
                .get()
                .await()

            val lista = querySnapshot.toObjects(Rota::class.java)
            UIstatus.Sucesso(lista)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao filtrar por data: ${e.message}")
        }
    }

    // --- MÉTODOS DE STATUS E FOTOS ---

    override suspend fun finalizarRotaComSucesso(idRota: String, imageUri: Uri): UIstatus<String> {
        return try {
            val urlFoto = fazerUploadFoto(idRota, imageUri)
            colecaoRotas.document(idRota).update(
                mapOf(
                    "comprovanteUrl" to urlFoto,
                    ConstantesFirebase.CAMPO_STATUS to "CONCLUIDA",
                    "observacao" to null
                )
            ).await()
            UIstatus.Sucesso("Entrega finalizada!")
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao finalizar: ${e.message}")
        }
    }

    override suspend fun reportarProblemaRota(idRota: String, motivo: String, imageUri: Uri?): UIstatus<String> {
        return try {
            val updates = mutableMapOf<String, Any?>(
                ConstantesFirebase.CAMPO_STATUS to "PROBLEMA",
                "observacao" to motivo
            )
            imageUri?.let { updates["comprovanteUrl"] = fazerUploadFoto(idRota, it) }

            colecaoRotas.document(idRota).update(updates).await()
            UIstatus.Sucesso("Problema reportado!")
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao reportar: ${e.message}")
        }
    }

    private suspend fun fazerUploadFoto(idRota: String, imageUri: Uri): String {
        val storageRef = firebaseStorage.reference
            .child(ConstantesFirebase.STORAGE_COMPROVANTES)
            .child(idRota)
            .child("comprovante_${System.currentTimeMillis()}.jpg")

        val dadosImagem = ImageHelper.prepararParaUpload(context, imageUri)
            ?: throw Exception("Erro ao processar imagem")

        storageRef.putBytes(dadosImagem).await()
        return storageRef.downloadUrl.await().toString()
    }
    // Métodos legados (mantidos para compatibilidade se necessário)
    override suspend fun atualizarStatus(idRota: String, status: String, observacao: String?): UIstatus<Boolean> {
        val res = reportarProblemaRota(idRota, observacao ?: "", null)
        return if (res is UIstatus.Sucesso) UIstatus.Sucesso(true) else UIstatus.Erro("Erro")
    }

    override suspend fun enviarComprovante(idRota: String, imageUri: Uri): UIstatus<String> {
        return finalizarRotaComSucesso(idRota, imageUri)
    }

    override fun listarPorMotoristaRealTime(
        idMotorista: String,
        onResult: (UIstatus<List<Rota>>) -> Unit
    ) {
        onResult(UIstatus.Carregando)

        colecaoRotas
            .whereEqualTo("idMotorista", idMotorista)
            .addSnapshotListener { snapshot, erro ->
                if (erro != null) {
                    onResult(UIstatus.Erro("Erro Firebase: ${erro.message}"))
                    return@addSnapshotListener
                }

                val lista = snapshot?.toObjects(Rota::class.java) ?: emptyList()
                onResult(UIstatus.Sucesso(lista))
            }
    }

    override fun listarPorMotoristaEDataRealTime(
        uid: String,
        data: String,
        callback: (UIstatus<List<Rota>>) -> Unit
    ) {
        callback(UIstatus.Carregando)

        firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_ROTAS)
            .whereEqualTo(ConstantesFirebase.CAMPO_ID_MOTORISTA, uid)
            .whereEqualTo(ConstantesFirebase.CAMPO_DATA_FORMATADA, data)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    callback(UIstatus.Erro("Erro: ${error.message}"))
                    return@addSnapshotListener
                }
                val lista = snapshot?.toObjects(Rota::class.java) ?: emptyList()
                callback(UIstatus.Sucesso(lista))
            }
    }

    override suspend fun buscarMotorista(uid: String): Motorista? {
        return try {
            val document = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_USUARIOS_MOTORISTA)
                .document(uid)
                .get()
                .await()

            if (document.exists()) {
                document.toObject(Motorista::class.java)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun recuperarTelefoneAdmin(idGestor: String): String? {
        return try {
            val document = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_USUARIOS)
                .document(idGestor)
                .get()
                .await()

            if (document.exists()) {
                document.getString("telefone")
            } else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun arquivarRotas(idMotorista: String, data: String): UIstatus<Boolean> {
        return try {
            val query = firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_ROTAS)
                .whereEqualTo(ConstantesFirebase.CAMPO_ID_MOTORISTA, idMotorista)
                .whereEqualTo(ConstantesFirebase.CAMPO_DATA_FORMATADA, data)
                .get()
                .await()

            val batch = firebaseFirestore.batch()
            for (documento in query.documents) {
                // Atualiza o campo de arquivamento específico do motorista
                batch.update(documento.reference, ConstantesFirebase.CAMPO_ARQUIVADA_MOTORISTA, true)
            }
            batch.commit().await()
            UIstatus.Sucesso(true)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao arquivar histórico: ${e.message}")
        }
    }




    // No RotaRepositoryImpl.kt (Core)
    /*override suspend fun atualizarStatusAdministrador(idMotorista: String, data: Long, novoStatus: String): UIstatus<Boolean> {
        return try {
            val query = colecaoRotas
                .whereEqualTo("idMotorista", idMotorista)
                .whereEqualTo("dataPrevista", data)
                .get()
                .await()

            val batch = firebaseFirestore.batch()
            for (documento in query.documents) {
                batch.update(documento.reference, "status", novoStatus)
                // Se quiser que suma de vez de todas as listas do motorista:
                batch.update(documento.reference, "arquivada", true)
            }
            batch.commit().await()
            UIstatus.Sucesso(true)
        } catch (e: Exception) {
            UIstatus.Erro(e.message ?: "Erro ao atualizar status")
        }
    }*/


}