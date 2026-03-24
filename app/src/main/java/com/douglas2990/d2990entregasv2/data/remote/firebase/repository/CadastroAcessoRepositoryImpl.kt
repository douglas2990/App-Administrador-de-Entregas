package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import android.content.Context
import com.example.core.UIstatus
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class CadastroAcessoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mainAuth: FirebaseAuth, // Instância do Guilherme (Admin)
    private val firestore: FirebaseFirestore
) : ICadastroAcessoRepository {

    override suspend fun criarAcessoMotorista(email: String, senha: String, nome: String): UIstatus<String> {
        return try {
            // 1. Pegamos o ID do Guilherme (Admin) na instância principal
            val idGestorLogado = mainAuth.currentUser?.uid
                ?: return UIstatus.Erro("Administrador não autenticado.")

            // 2. Criar/Recuperar a instância secundária
            val options = FirebaseApp.getInstance().options
            val secondaryApp = try {
                FirebaseApp.initializeApp(context, options, "AdminControlApp")
            } catch (e: Exception) {
                FirebaseApp.getInstance("AdminControlApp")
            }

            // 3. Pegar Auth e Firestore vinculados APENAS ao app secundário
            val secondaryAuth = FirebaseAuth.getInstance(secondaryApp)
            val secondaryFirestore = FirebaseFirestore.getInstance(secondaryApp)

            // 4. Criar o motorista no Auth secundário
            val authResult = secondaryAuth.createUserWithEmailAndPassword(email, senha).await()
            val novoMotoristaUid = authResult.user?.uid ?: return UIstatus.Erro("Falha ao gerar UID.")

            // 5. Montar os dados
            val dadosMotorista = mapOf(
                "id" to novoMotoristaUid,
                "email" to email,
                "nome" to nome,
                "idGestor" to idGestorLogado, // Vincula ao Guilherme
                "status" to "ativo",
                "dataCadastro" to com.google.firebase.Timestamp.now()
            )

            // 6. GRAVAR NO FIRESTORE SECUNDÁRIO
            // Isso é vital: o Firebase verá que quem está gravando é o motorista logado no SecondaryApp
            secondaryFirestore.collection("usuarios_motoristas")
                .document(novoMotoristaUid)
                .set(dadosMotorista)
                .await()

            // 7. Limpeza profissional
            secondaryAuth.signOut()
            secondaryApp.delete()

            UIstatus.Sucesso(novoMotoristaUid)

        } catch (e: Exception) {
            // Log para você ver o erro real no Logcat do Android Studio
            android.util.Log.e("CADASTRO_ERRO", "Erro detalhado: ${e.message}", e)
            UIstatus.Erro("Erro ao criar acesso profissional: ${e.localizedMessage}")
        }
    }
}