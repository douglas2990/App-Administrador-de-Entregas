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


            // --- VERIFICAÇÃO VIA KOTLIN ---
            val cnpjJaExiste = verificarCnpjExistente(empresa.cnpj, null)

            if (cnpjJaExiste) {
                return uiStatus.invoke(UIstatus.Erro("Você já cadastrou uma empresa com este CNPJ"))
            }
            // ------------------------------


            val refEmpresa = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_EMPRESA)
                .document( idUser )
                .collection("itens")
                .document()

            val idEmpresa = refEmpresa.id
            empresa.id = idEmpresa
            refEmpresa.set( empresa ).await()

            uiStatus.invoke( UIstatus.Sucesso(idEmpresa) )

        }catch (erroAtualizarCampo: Exception){
            uiStatus.invoke(UIstatus.Erro("Erro ao atualizar dados da empresa"))
        }

    }

    override suspend fun atualizar(
        empresa: Empresa,
        uiStatus: (UIstatus<String>) -> Unit
    ) {

        android.util.Log.d("TESTE_ID", "ID recebido para atualizar: '${empresa.id}'")

        try {
            val idUser = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke(UIstatus.Erro("Usuário não está logado"))

            // --- NOVA VERIFICAÇÃO NA ATUALIZAÇÃO ---
            val cnpjJaExisteEmOutra = verificarCnpjExistente(empresa.cnpj, empresa.id)
            if (cnpjJaExisteEmOutra) {
                return uiStatus.invoke(UIstatus.Erro("Este CNPJ já está vinculado a outra empresa"))
            }
            // ---------------------------------------

            val refEmpresa = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_EMPRESA)
                .document(idUser)
                .collection("itens")
                .document(empresa.id)

            refEmpresa.update(empresa.toMap()).await()
            uiStatus.invoke(UIstatus.Sucesso(empresa.id))

        } catch (e: Exception) {
            uiStatus.invoke(UIstatus.Erro("Não foi possível atualizar dados"))
        }
    }

    override suspend fun listar(
        uiStatus: (UIstatus<List<Empresa>>) -> Unit
    ) {
        try {

            val idUser = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke( UIstatus.Erro("Usuário não está logado") )

            val refProduto = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_EMPRESA)
                .document( idUser )
                .collection("itens")

            val querySnapshot = refProduto.get().await()

            if( querySnapshot.documents.isNotEmpty() ){
                val listaEmpresa = querySnapshot.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject( Empresa::class.java )
                }
                uiStatus.invoke(UIstatus.Sucesso( listaEmpresa ))
            }else{
                uiStatus.invoke(UIstatus.Sucesso(emptyList()))
            }
        }catch (erroRecuperarLoja: Exception){
            uiStatus.invoke(UIstatus.Erro("Erro ao acessar empresas cadastradas"))
        }
    }

    override suspend fun recuperarEmpresaPeloId(
        idEmpresa: String,
        uiStatus: (UIstatus<Empresa>) -> Unit
    ) {
        try {

            val idUser = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke( UIstatus.Erro("Usuário não está logado") )

            val refProduto = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_EMPRESA)
                .document( idUser )
                .collection("itens")
                .document( idEmpresa )

            val documentSnapshot = refProduto.get().await()
            if( documentSnapshot.exists() ){
                val empresa = documentSnapshot.toObject( Empresa::class.java )
                if(empresa != null){
                    uiStatus.invoke(UIstatus.Sucesso(empresa))
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
        idEmpresa: String,
        uiStatus: (UIstatus<Boolean>) -> Unit
    ) {
        try {

            val idUser = firebaseAuth.currentUser?.uid ?:
            return uiStatus.invoke( UIstatus.Erro("Usuário não está logado") )

            val refEmpresa = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_EMPRESA)
                .document( idUser )
                .collection("itens")
                .document( idEmpresa )

            refEmpresa.delete().await()
            uiStatus.invoke( UIstatus.Sucesso( true ) )

        }catch ( erroAtualizarCampo: Exception ){
            uiStatus.invoke(UIstatus.Erro("Erro ao remover produto"))
        }

    }

    override suspend fun verificarCnpjExistente(cnpj: String, idEmpresaAtual: String?): Boolean {
        return try {
            val idUser = firebaseAuth.currentUser?.uid ?: return false

            val querySnapshot = firebaseFirestore
                .collection(ConstantesFirebase.FIRESTORE_EMPRESA)
                .document(idUser)
                .collection("itens")
                .whereEqualTo("cnpj", cnpj)
                .get()
                .await()

            if (querySnapshot.isEmpty) return false

            // Se encontrou algo, verificamos se o documento achado tem um ID diferente do atual
            val documentoConflitante = querySnapshot.documents.any { doc ->
                doc.id != idEmpresaAtual
            }

            return documentoConflitante
        } catch (e: Exception) {
            false
        }
    }
}
