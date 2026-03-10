package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import com.douglas2990.d2990entregasv2.model.Motorista
import com.example.core.UIstatus
import com.example.core.util.ConstantesFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MotoristaRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : IMotoristaRepository {

    // Referência centralizada para a coleção raiz
    private val colecaoMotoristas = firebaseFirestore
        .collection(ConstantesFirebase.FIRESTORE_USUARIOS_MOTORISTA)

    override suspend fun salvar(motorista: Motorista): UIstatus<String> {
        return try {
            val idGestorLogado = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Gestor não autenticado. Faça login novamente.")

            // 1. Gera um novo documento na coleção raiz 'usuarios_motoristas'
            val refMotorista = colecaoMotoristas.document()

            // 2. Prepara o objeto com o ID do documento e o vínculo com o Gestor
            val motoristaComIds = motorista.copy(
                id = refMotorista.id,
                idGestor = idGestorLogado // Crucial para as Rules do Firebase
            )

            // 3. Salva no Firestore
            refMotorista.set(motoristaComIds).await()

            UIstatus.Sucesso(refMotorista.id)

        } catch (e: Exception) {
            UIstatus.Erro("Erro ao cadastrar motorista: ${e.message}")
        }
    }

    override suspend fun listar(): UIstatus<List<Motorista>> {
        return try {
            val idGestorLogado = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Usuário não autenticado")

            // IMPORTANTE: Filtramos para que o Guilherme veja apenas os motoristas dele
            val querySnapshot = colecaoMotoristas
                .whereEqualTo("idGestor", idGestorLogado)
                .get()
                .await()

            val lista = querySnapshot.documents.mapNotNull { documento ->
                documento.toObject(Motorista::class.java)
            }

            UIstatus.Sucesso(lista)

        } catch (e: Exception) {
            android.util.Log.e("FIREBASE_ERROR", "Erro ao listar: ${e.message}", e)
            UIstatus.Erro("Erro ao carregar motoristas: ${e.localizedMessage}")
        }
    }

    override suspend fun remover(idMotorista: String): UIstatus<Boolean> {
        return try {
            // A regra de segurança no Firebase impedirá a deleção
            // se o idGestor do documento não for o UID de quem está tentando deletar
            colecaoMotoristas
                .document(idMotorista)
                .delete()
                .await()

            UIstatus.Sucesso(true)

        } catch (e: Exception) {
            UIstatus.Erro("Não foi possível excluir o motorista: ${e.message}")
        }
    }
}