package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.core.UIstatus
import com.example.core.model.Motorista
import com.example.core.model.Rota
import com.example.core.repository.IRotaRepository
import com.example.core.repository.ImageHelper
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
                timeZone = java.util.TimeZone.getTimeZone("UTC") // GARANTE O DIA CORRETO
            }
            val dataFormatada = rota.dataPrevista?.let { sdf.format(Date(it)) } ?: ""

            // Criamos a cópia final ÚNICA com todos os dados
            val rotaFinal = rota.copy(
                id = refRota.id,
                idGestor = idGestorFinal,
                dataCriacao = dataCriacaoFinal,
                dataPrevistaFormatada = dataFormatada, // Campo novo para o filtro
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
            // CORREÇÃO: Removemos o .whereEqualTo("idGestor", idLogado)
            // O motorista deve ver tudo que foi destinado ao ID dele, não importa quem criou.
            val querySnapshot = colecaoRotas
                .whereEqualTo("idMotorista", idMotorista)
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
            // Buscamos as rotas que o motorista ainda precisa fazer
            val querySnapshot = colecaoRotas
                .whereEqualTo("idMotorista", idMotorista)
                .whereIn("status", listOf("PENDENTE", "PROBLEMA"))
                .get()
                .await()

            // O erro costuma acontecer aqui se o objeto Rota não tiver um construtor vazio
            // ou se algum campo Long for nulo no banco.
            val rotas = querySnapshot.toObjects(Rota::class.java)

            val datasUnicas = rotas.mapNotNull { it.dataPrevista }
                .distinct()
                .sorted()

            UIstatus.Sucesso(datasUnicas)
        } catch (e: Exception) {
            // Adicionei o log do erro para você ver exatamente o que falhou no Logcat
            UIstatus.Erro("Erro ao buscar datas: ${e.message}")
        }
    }

    override suspend fun listarPorDataEMotorista(idMotorista: String, data: Long): UIstatus<List<Rota>> {
        return try {
            // Filtro direto: O motorista logado quer ver as rotas DELE para a DATA X
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

        // Chamamos o seu Helper para transformar a URI em um ByteArray de 720p
        val dadosImagem = ImageHelper.prepararParaUpload(context, imageUri)
            ?: throw Exception("Erro ao processar imagem")

        //storageRef.putFile(imageUri).await()
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

        // Remova o .orderBy se não quiser criar índices no console agora
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
        data: String, // Recebe a String da Agenda (ex: "07/04/2026")
        callback: (UIstatus<List<Rota>>) -> Unit
    ) {
        callback(UIstatus.Carregando)

        // USAMOS 'firebaseFirestore' (que é o nome da sua variável lá em cima)
        firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_ROTAS)
            .whereEqualTo("idMotorista", uid)
            // ATENÇÃO: Se no Firestore você salva a data como Long,
            // a comparação .whereEqualTo com String vai retornar vazio.
            // O ideal é que o seu objeto Rota tenha o campo 'dataPrevistaFormatada'
            // ou que a gente converta a String de volta para Long aqui.
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

    // Dentro da classe RotaRepositoryImpl
    override suspend fun buscarMotorista(uid: String): Motorista? {
        return try {
            // USANDO A CONSTANTE CORRETA: FIRESTORE_USUARIOS_MOTORISTA
            val document = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_USUARIOS_MOTORISTA)
                .document(uid)
                .get()
                .await()

            if (document.exists()) {
                val motorista = document.toObject(Motorista::class.java)
                // Log para conferir no Logcat se a empresa veio preenchida
                println("FIREBASE_SUCESSO: Motorista encontrado! Empresa: ${motorista?.nomeEmpresa}")
                motorista
            } else {
                println("FIREBASE_AVISO: UID $uid não encontrado em ${ConstantesFirebase.FIRESTORE_USUARIOS_MOTORISTA}")
                null
            }
        } catch (e: Exception) {
            println("FIREBASE_ERRO: ${e.message}")
            null
        }
    }

}