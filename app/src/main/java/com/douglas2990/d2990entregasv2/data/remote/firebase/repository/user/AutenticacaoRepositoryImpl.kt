package com.douglas2990.d2990entregasv2.data.remote.firebase.repository.user

import com.douglas2990.d2990entregasv2.model.user.Usuario
import com.example.core.UIstatus
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

    override suspend fun recuperarDadosUsuarioLogado(
        uiStatus: (UIstatus<Usuario>) -> Unit
    ) {
        try {

            val idUsuario = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke( UIstatus.Erro("Usuário não está logado") )

            val refUsuario = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_USUARIOS)
                .document( idUsuario )

            val documentSnapshot = refUsuario.get().await()
            if( documentSnapshot.exists() ){
                val usuario = documentSnapshot.toObject( Usuario::class.java )
                if(usuario != null){
                    uiStatus.invoke(UIstatus.Sucesso(usuario))
                }else{
                    uiStatus.invoke(UIstatus.Erro("Erro ao converter dados do usuário"))
                }
            }else{
                uiStatus.invoke(UIstatus.Erro("Não existem dados para o usuário"))
            }
        }catch (erroRecuperarLoja: Exception){
            uiStatus.invoke(UIstatus.Erro("Erro ao recuperar dados do usuário"))
        }
    }

    override suspend fun atualizarUsuario(
        usuario: Usuario,
        uiStatus: (UIstatus<String>) -> Unit
    ) {
        try {

            val idUsuario = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke( UIstatus.Erro("Usuário não está logado") )

            val refUsuario = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_USUARIOS)
                .document( idUsuario )
            refUsuario.update( usuario.mapToUsuarioFirestore() ).await()

            uiStatus.invoke(UIstatus.Sucesso( idUsuario ))

        }catch (erroAtualizarCampo: Exception){
            uiStatus.invoke(UIstatus.Erro("Erro ao atualizar dados do usuário"))
        }

    }

    override suspend fun cadastrarUsuario(
        usuario: Usuario,
        uiStatus: (UIstatus<Boolean>)->Unit
    ) {

        try {

            firebaseAuth.createUserWithEmailAndPassword(
                usuario.email, usuario.senha
            ).await()

            val idUsuario = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke( UIstatus.Erro("Usuário não está logado") )

            //Salvar dados usuario no Firestore
            usuario.id = idUsuario
            val usuariosRef = firebaseFirestore
                .collection( ConstantesFirebase.FIRESTORE_USUARIOS )
                .document( idUsuario )

            usuariosRef.set(
                usuario.mapToUsuarioFirestore()
            ).await()

            uiStatus.invoke(
                UIstatus.Sucesso( true )
            )

        }catch ( erroUsuarioJaCadastrado: FirebaseAuthUserCollisionException){
            uiStatus.invoke(
                UIstatus.Erro("Usuário já cadastrado!")
            )
        }catch ( erroEmailInvalido: FirebaseAuthInvalidCredentialsException){
            uiStatus.invoke(
                UIstatus.Erro("E-mail está inválido, digite outro e-mail!")
            )
        }catch ( erroSenhaFraca: FirebaseAuthWeakPasswordException){
            uiStatus.invoke(
                UIstatus.Erro("Sua senha está muito fraca, digite mais caracteres!")
            )
        }catch ( erroPadrao: Exception ){
            uiStatus.invoke(
                UIstatus.Erro("Erro ao fazer seu cadastro, tente novamente!")
            )
        }

    }

    override suspend fun logarUsuario(
        usuario: Usuario,
        uiStatus: (UIstatus<Boolean>)->Unit
    ) {
        try {
            val retorno = firebaseAuth.signInWithEmailAndPassword(
                usuario.email, usuario.senha
            ).await() != null

            if( retorno ){//true
                uiStatus.invoke(
                    UIstatus.Sucesso( true )
                )
            }

        }catch ( erroUsuarioInvalido: FirebaseAuthInvalidUserException){
            uiStatus.invoke(
                UIstatus.Erro("E-mail inválido, usuário não cadastrado!")
            )
        }catch ( erroSenhaInvalida: FirebaseAuthInvalidCredentialsException ){
            uiStatus.invoke(
                UIstatus.Erro("A senha digitada está errada")
            )
        }catch ( erroPadrao: Exception ){
            uiStatus.invoke(
                UIstatus.Erro("Dados de acesso errado, tente novamente!")
            )
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

}