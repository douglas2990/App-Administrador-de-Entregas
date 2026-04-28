package com.douglas2990.d2990entregasv2.data.remote.firebase.repository.user

import com.douglas2990.d2990entregasv2.model.user.Usuario
import com.example.core.UIstatus
import com.example.core.model.SolicitacaoAcesso
import com.example.core.util.ConstantesFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AutenticacaoRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
): IAutenticacaoRepository {

    override suspend fun recuperarDadosUsuarioLogado(uiStatus: (UIstatus<Usuario>) -> Unit) {
        try {
            val idUsuario = firebaseAuth.currentUser?.uid ?: return uiStatus.invoke(UIstatus.Erro("Deslogado"))
            val doc = firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_USUARIOS).document(idUsuario).get().await()

            if (doc.exists()) {
                val user = doc.toObject(Usuario::class.java)
                if (user != null) uiStatus.invoke(UIstatus.Sucesso(user))
                else uiStatus.invoke(UIstatus.Erro("Erro de conversão"))
            } else {
                uiStatus.invoke(UIstatus.Erro("Usuário não encontrado"))
            }
        } catch (e: Exception) {
            uiStatus.invoke(UIstatus.Erro("Erro ao recuperar dados"))
        }
    }

    override suspend fun solicitarAcesso(email: String, uiStatus: (UIstatus<Boolean>) -> Unit) {
        try {
            val dados = mapOf("email" to email, "status" to "PENDENTE")
            firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_SOLICITACOES)
                .document(email)
                .set(dados).await()
            uiStatus(UIstatus.Sucesso(true))
        } catch (e: Exception) {
            uiStatus(UIstatus.Erro("Erro ao solicitar"))
        }
    }

    override suspend fun atualizarUsuario(usuario: Usuario, uiStatus: (UIstatus<String>) -> Unit) {
        try {
            val idUsuario = firebaseAuth.currentUser?.uid ?: return uiStatus.invoke(UIstatus.Erro("Deslogado"))
            firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_USUARIOS)
                .document(idUsuario).update(usuario.mapToUsuarioFirestore()).await()
            uiStatus.invoke(UIstatus.Sucesso(idUsuario))
        } catch (e: Exception) {
            uiStatus.invoke(UIstatus.Erro("Erro ao atualizar"))
        }
    }

    override suspend fun cadastrarUsuario(
        usuario: Usuario,
        uiStatus: (UIstatus<Boolean>) -> Unit
    ) {
        try {
            // 1. CHECAGEM DE WHITELIST: O e-mail está na coleção 'autorizados'?
            val autorizadoDoc = firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_AUTORIZADOS)
                .document(usuario.email)
                .get()
                .await()

            if (!autorizadoDoc.exists()) {
                // 2. SOLICITAÇÃO: Se não está autorizado, cria o pedido para o ModuloDeveloper
                val solicitacao = SolicitacaoAcesso(
                    id = usuario.email,
                    nome = usuario.nome,
                    email = usuario.email,
                    dataSolicitacao = System.currentTimeMillis()
                )

                firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_SOLICITACOES)
                    .document(usuario.email)
                    .set(solicitacao)
                    .await()

                // Avisa a UI que o acesso está pendente de aprovação manual
                return uiStatus.invoke(UIstatus.Erro("Acesso pendente. Solicitação enviada ao desenvolvedor. Chame no WhatsApp!"))
            }

            // 3. CADASTRO REAL: Se chegou aqui, o e-mail está autorizado
            firebaseAuth.createUserWithEmailAndPassword(usuario.email, usuario.senha).await()

            val idUsuario = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke(UIstatus.Erro("Falha ao recuperar ID de autenticação"))

            usuario.id = idUsuario
            firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_USUARIOS)
                .document(idUsuario)
                .set(usuario.mapToUsuarioFirestore())
                .await()

            uiStatus.invoke(UIstatus.Sucesso(true))

        } catch (e: FirebaseAuthUserCollisionException) {
            uiStatus.invoke(UIstatus.Erro("Usuário já cadastrado!"))
        } catch (e: FirebaseAuthWeakPasswordException) {
            uiStatus.invoke(UIstatus.Erro("Sua senha está muito fraca!"))
        } catch (e: Exception) {
            uiStatus.invoke(UIstatus.Erro("Erro ao processar cadastro: ${e.localizedMessage}"))
        }
    }

    override suspend fun logarUsuario(
        usuario: Usuario,
        uiStatus: (UIstatus<Boolean>) -> Unit
    ) {
        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(usuario.email, usuario.senha).await()
            val uid = authResult.user?.uid ?: throw Exception("Erro ao recuperar UID")

            // VERIFICAÇÃO DE PERFIL: Garante que motorista não entra no app Admin
            val doc = firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_USUARIOS)
                .document(uid).get().await()

            if (doc.exists()) {
                uiStatus.invoke(UIstatus.Sucesso(true))
            } else {
                firebaseAuth.signOut()
                uiStatus.invoke(UIstatus.Erro("Acesso Negado: App exclusivo para Administradores."))
            }
        } catch (e: Exception) {
            uiStatus.invoke(UIstatus.Erro("Dados incorretos ou sem permissão de acesso."))
        }
    }
    override fun verificarUsuarioLogado(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun recuperarIdUsuarioLogado(): String {
        return firebaseAuth.currentUser?.uid ?: ""
    }

    override fun deslogarUsuario() {
        firebaseAuth.signOut()
    }

    override fun ouvirStatusAprovacao(email: String, retornoStatus: (String) -> Unit) {
        firebaseFirestore.collection(ConstantesFirebase.FIRESTORE_SOLICITACOES)
            .document(email)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                val status = snapshot?.getString("status") ?: "PENDENTE"
                retornoStatus(status)
            }
    }

}