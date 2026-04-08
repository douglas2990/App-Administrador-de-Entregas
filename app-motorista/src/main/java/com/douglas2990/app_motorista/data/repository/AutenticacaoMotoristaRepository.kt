package com.douglas2990.app_motorista.data.repository

import com.example.core.UIstatus
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

            // Verifica se o usuário é realmente um motorista no Firestore
            val doc = firestore.collection("usuarios_motoristas").document(uid).get().await()
            
            if (doc.exists()) {
                UIstatus.Sucesso(uid)
            } else {
                firebaseAuth.signOut()
                UIstatus.Erro("Este usuário não tem permissão de motorista.")
            }
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao fazer login: ${e.message}")
        }
    }

    override fun usuarioLogado(): Boolean = firebaseAuth.currentUser != null
}
