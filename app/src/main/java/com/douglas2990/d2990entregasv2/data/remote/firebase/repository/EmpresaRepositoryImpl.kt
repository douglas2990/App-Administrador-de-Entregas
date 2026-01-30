package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import com.douglas2990.d2990entregasv2.model.Empresa
import com.example.core.UIstatus
import com.example.core.util.ConstantesFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EmpresaRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : IEmpresaRepository {
    override suspend fun salvar(
        empresa: Empresa,
        uiStatus: (UIstatus<String>) -> Unit
    ) {
        try {

            val idUser = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke( UIstatus.Erro("Usuário não está logado") )

            val refEmpresa = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_EMPRESA)
                .document( idUser )
                .collection("itens")
                .document()

            val idProduto = refEmpresa.id
            empresa.id = idProduto
            refEmpresa.set( empresa ).await()

            uiStatus.invoke( UIstatus.Sucesso(idProduto) )

        }catch (erroAtualizarCampo: Exception){
            uiStatus.invoke(UIstatus.Erro("Erro ao atualizar dados do produto"))
        }

    }

    override suspend fun atualizar(
        empresa: Empresa,
        uiStatus: (UIstatus<String>) -> Unit
    ) {
        try {

            val idLoja = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke( UIstatus.Erro("Usuário não está logado") )

            val refProduto = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_PRODUTOS)
                .document( idLoja )
                .collection("itens")
                .document( empresa.id )

            refProduto.update( empresa.toMap() ).await()

            uiStatus.invoke( UIstatus.Sucesso( empresa.id ) )

        }catch (erroAtualizarCampo: Exception){
            uiStatus.invoke(UIstatus.Erro("Erro ao atualizar dados do produto"))
        }

    }

    override suspend fun listar(
        uiStatus: (UIstatus<List<Empresa>>) -> Unit
    ) {
        try {

            val idLoja = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke( UIstatus.Erro("Usuário não está logado") )

            val refProduto = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_PRODUTOS)
                .document( idLoja )
                .collection("itens")

            val querySnapshot = refProduto.get().await()

            if( querySnapshot.documents.isNotEmpty() ){
                val listaProdutos = querySnapshot.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject( Empresa::class.java )
                }
                uiStatus.invoke(UIstatus.Sucesso( listaProdutos ))
            }else{
                uiStatus.invoke(UIstatus.Sucesso(emptyList()))
            }
        }catch (erroRecuperarLoja: Exception){
            uiStatus.invoke(UIstatus.Erro("Erro ao recuperar produtos"))
        }
    }

    override suspend fun recuperarEmpresaPeloId(
        idProduto: String,
        uiStatus: (UIstatus<Empresa>) -> Unit
    ) {
        try {

            val idLoja = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke( UIstatus.Erro("Usuário não está logado") )

            val refProduto = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_PRODUTOS)
                .document( idLoja )
                .collection("itens")
                .document( idProduto )

            val documentSnapshot = refProduto.get().await()
            if( documentSnapshot.exists() ){
                val produto = documentSnapshot.toObject( Empresa::class.java )
                if(produto != null){
                    uiStatus.invoke(UIstatus.Sucesso(produto))
                }else{
                    uiStatus.invoke(UIstatus.Erro("Erro ao converter dados do produto"))
                }
            }else{
                uiStatus.invoke(UIstatus.Erro("Não existem dados para o produto"))
            }
        }catch (erroRecuperarLoja: Exception){
            uiStatus.invoke(UIstatus.Erro("Erro ao recuperar dados do produto"))
        }
    }

    override suspend fun remover(
        idProduto: String,
        uiStatus: (UIstatus<Boolean>) -> Unit
    ) {
        try {

            val idLoja = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke( UIstatus.Erro("Usuário não está logado") )

            val refProduto = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_PRODUTOS)
                .document( idLoja )
                .collection("itens")
                .document( idProduto )

            refProduto.delete().await()
            uiStatus.invoke( UIstatus.Sucesso( true ) )

        }catch ( erroAtualizarCampo: Exception ){
            uiStatus.invoke(UIstatus.Erro("Erro ao remover produto"))
        }

    }
}
