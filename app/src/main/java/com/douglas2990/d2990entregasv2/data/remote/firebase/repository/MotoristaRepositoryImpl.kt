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

    override suspend fun salvar(motorista: Motorista): UIstatus<String> {
        return try {
            val idGestor = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Usuário gestor não autenticado")

            // Referência: usuarios / {idGestor} / motoristas / {idMotorista}
            val refMotorista = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_USUARIOS)
                .document(idGestor)
                .collection("motoristas") // Sub-coleção isolada
                .document()

            val motoristaComId = motorista.copy(id = refMotorista.id)

            refMotorista.set(motoristaComId).await()
            UIstatus.Sucesso(refMotorista.id)

        } catch (e: Exception) {
            UIstatus.Erro("Erro ao cadastrar motorista: ${e.message}")
        }
    }

    override suspend fun listar(): UIstatus<List<Motorista>> {
        return try {
            val idGestor = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Usuário não autenticado")

            val querySnapshot = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_USUARIOS)
                .document(idGestor)
                .collection("motoristas")
                .get()
                .await()

            val lista = querySnapshot.documents.mapNotNull { it.toObject(Motorista::class.java) }
            UIstatus.Sucesso(lista)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao listar seus motoristas")
        }
    }

    override suspend fun remover(idMotorista: String): UIstatus<Boolean> {
        return try {
            // 1. Recupera o ID do Gestor logado para garantir que ele só delete da PRÓPRIA carteira
            val idGestor = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Sessão expirada. Autentique-se novamente.")

            // 2. Referência ao documento dentro da "caixa" do gestor atual
            val refMotorista = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_USUARIOS)
                .document(idGestor)
                .collection("motoristas")
                .document(idMotorista)

            // 3. Executa a remoção e aguarda a conclusão (await)
            refMotorista.delete().await()

            UIstatus.Sucesso(true)
        } catch (e: Exception) {
            UIstatus.Erro("Não foi possível excluir o motorista: ${e.message}")
        }
    }
}