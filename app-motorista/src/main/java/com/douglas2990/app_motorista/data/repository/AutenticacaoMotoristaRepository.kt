package com.douglas2990.app_motorista.data.repository

import com.example.core.UIstatus
import com.example.core.util.ConstantesFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface IAutenticacaoMotoristaRepository {
    suspend fun login(email: String, senha: String): UIstatus<String>
    fun usuarioLogado(): Boolean
}

class AutenticacaoMotoristaRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : IAutenticacaoMotoristaRepository {

    override suspend fun login(email: String, senha: String): UIstatus<String> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, senha).await()
            val uid = result.user?.uid ?: return UIstatus.Erro("Usuário não encontrado")

            // USANDO A CONSTANTE: ConstantesFirebase.FIRESTORE_USUARIOS_MOTORISTA
            val doc = firestore.collection(ConstantesFirebase.FIRESTORE_USUARIOS_MOTORISTA)
                .document(uid)
                .get()
                .await()

            if (doc.exists()) {
                UIstatus.Sucesso(uid)
            } else {
                // Se o e-mail existe no Auth mas não está na coleção de motoristas,
                // significa que é um Admin tentando logar no app do motorista.
                firebaseAuth.signOut()
                UIstatus.Erro("Acesso Negado: Use o aplicativo de Administrador.")
            }
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao fazer login: Dados de acesso inválidos.")
        }
    }

    override fun usuarioLogado(): Boolean = firebaseAuth.currentUser != null
}
