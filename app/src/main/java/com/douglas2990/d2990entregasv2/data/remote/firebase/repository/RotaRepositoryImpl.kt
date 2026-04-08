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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class RotaRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : IRotaRepository {

    private val colecaoRotas = firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_ROTAS)

    // --- MÉTODOS DE ESCRITA (ADMIN) ---

    override suspend fun salvar(rota: Rota): UIstatus<String> {
        return try {
            val idLogado = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Usuário não autenticado")

            val refRota = if (rota.id.isEmpty()) colecaoRotas.document() else colecaoRotas.document(rota.id)

            // Lógica de datas e IDs
            val idGestorFinal = if (rota.idGestor.isEmpty()) idLogado else rota.idGestor
            val dataCriacaoFinal = if (rota.id.isEmpty()) System.currentTimeMillis() else rota.dataCriacao


            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                timeZone = java.util.TimeZone.getTimeZone("UTC") // GARANTE O DIA CORRETO
            }
            val dataFormatada = rota.dataPrevista?.let { sdf.format(Date(it)) } ?: ""

            val rotaFinal = rota.copy(
                id = refRota.id,
                idGestor = idGestorFinal,
                dataCriacao = dataCriacaoFinal,
                dataPrevistaFormatada = dataFormatada, // Campo crucial para o motorista
                status = if (rota.id.isEmpty()) "PENDENTE" else rota.status
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
                .whereEqualTo("idGestor", uidAtual)
                .orderBy("dataPrevista", Query.Direction.ASCENDING)
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
            val idLogado = firebaseAuth.currentUser?.uid ?: return UIstatus.Erro("Deslogado")

            // O Admin visualiza as rotas que ELE criou para aquele motorista específico
            val querySnapshot = colecaoRotas
                .whereEqualTo("idMotorista", idMotorista)
                .whereEqualTo("idGestor", idLogado) // Filtro de segurança
                .orderBy("dataPrevista", Query.Direction.DESCENDING)
                .get()
                .await()

            val lista = querySnapshot.toObjects(Rota::class.java)
            UIstatus.Sucesso(lista)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao carregar rotas do motorista: ${e.message}")
        }
    }

    // --- MÉTODOS DO CALENDÁRIO (MOTORISTA) ---

    override suspend fun listarDatasComRotas(idMotorista: String): UIstatus<List<Long>> {
        return try {
            val querySnapshot = colecaoRotas
                .whereEqualTo("idMotorista", idMotorista)
                .whereIn("status", listOf("PENDENTE", "PROBLEMA"))
                .get()
                .await()

            val rotas = querySnapshot.toObjects(Rota::class.java)
            val datasUnicas = rotas.mapNotNull { it.dataPrevista }
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
                .whereEqualTo("idMotorista", idMotorista)
                .whereEqualTo("dataPrevista", data)
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
                    "status" to "CONCLUIDA",
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
                "status" to "PROBLEMA",
                "observacao" to motivo
            )
            imageUri?.let { updates["comprovanteUrl"] = fazerUploadFoto(idRota, it) }

            colecaoRotas.document(idRota).update(updates).await()
            UIstatus.Sucesso("Problema reportado!")
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao reportar: ${e.message}")
        }
    }

    // Método privado auxiliar para upload
    private suspend fun fazerUploadFoto(idRota: String, imageUri: Uri): String {
        val storageRef = firebaseStorage.reference
            .child(ConstantesFirebase.STORAGE_COMPROVANTES)
            .child(idRota)
            .child("comprovante_${System.currentTimeMillis()}.jpg")

        storageRef.putFile(imageUri).await()
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
        // O Admin pode precisar desta versão real-time para monitorar o Guilherme
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
        firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_ROTAS)
            .whereEqualTo("idMotorista", uid)
            .whereEqualTo("dataPrevistaFormatada", data)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    callback(UIstatus.Erro("Erro: ${error.message}"))
                    return@addSnapshotListener
                }
                val lista = snapshot?.toObjects(Rota::class.java) ?: emptyList()
                callback(UIstatus.Sucesso(lista))
            }
    }
}
