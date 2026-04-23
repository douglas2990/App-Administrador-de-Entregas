package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import com.douglas2990.d2990entregasv2.model.Motorista
import com.example.core.UIstatus
import com.example.core.util.ConstantesFirebase
import com.google.firebase.FirebaseApp // Adicionado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MotoristaRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : IMotoristaRepository {

    private val colecaoMotoristas = firebaseFirestore
        .collection(ConstantesFirebase.FIRESTORE_USUARIOS_MOTORISTA)

    // NOVO MÉTODO: Agora recebe e-mail e senha para criar o acesso real
    override suspend fun salvar(motorista: Motorista, email: String, senha: String): UIstatus<String> {

        // 1. Criamos uma instância secundária para não deslogar o Guilherme
        val secondaryApp = try {
            FirebaseApp.getInstance("SecondaryApp")
        } catch (e: Exception) {
            val options = firebaseAuth.app.options
            FirebaseApp.initializeApp(firebaseAuth.app.applicationContext, options, "SecondaryApp")
        }

        val secondaryAuth = FirebaseAuth.getInstance(secondaryApp)

        return try {
            val idGestorLogado = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Gestor não autenticado.")

            // 2. Cria o usuário no Authentication (Instância Secundária)
            val authResult = secondaryAuth.createUserWithEmailAndPassword(email, senha).await()
            val novoUid = authResult.user?.uid ?: return UIstatus.Erro("Falha ao gerar UID")

            // 3. O SEGREDO: Usamos o UID do Auth como o ID do documento no Firestore
            val motoristaComIds = motorista.copy(
                id = novoUid, // ID agora é o UID real do motorista
                idGestor = idGestorLogado,
                tipo = "MOTORISTA"
            )

            // 4. Salva no Firestore usando o UID como nome do documento
            colecaoMotoristas.document(novoUid).set(motoristaComIds).await()

            // 5. Limpa a sessão secundária para não interferir no próximo cadastro
            secondaryAuth.signOut()

            UIstatus.Sucesso("Motorista cadastrado com sucesso!")

        } catch (e: Exception) {
            UIstatus.Erro("Erro ao cadastrar: ${e.message}")
        }
    }

    override suspend fun listar(): UIstatus<List<Motorista>> {
        return try {
            val idGestorLogado = firebaseAuth.currentUser?.uid
                ?: return UIstatus.Erro("Usuário não autenticado")

            val querySnapshot = colecaoMotoristas
                .whereEqualTo("idGestor", idGestorLogado)
                .get()
                .await()

            val lista = querySnapshot.documents.mapNotNull { documento ->
                documento.toObject(Motorista::class.java)
            }

            UIstatus.Sucesso(lista)
        } catch (e: Exception) {
            UIstatus.Erro("Erro ao carregar motoristas: ${e.localizedMessage}")
        }
    }

    override suspend fun remover(idMotorista: String): UIstatus<Boolean> {
        return try {
            colecaoMotoristas.document(idMotorista).delete().await()
            UIstatus.Sucesso(true)
        } catch (e: Exception) {
            UIstatus.Erro("Não foi possível excluir: ${e.message}")
        }
    }
}