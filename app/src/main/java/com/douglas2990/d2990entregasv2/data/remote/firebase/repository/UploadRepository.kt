package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import android.net.Uri
import com.example.core.UIstatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UploadRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun upload(
        local:String, nomeImagem: String, uriImagem: Uri
    ) : UIstatus<String> {

        try {
            /*
        * lojas
        *   + id_loja
        *       perfil
        *           imagem_perfil
        *           imagem_capa
        * */
            val idLoja = firebaseAuth.currentUser?.uid ?:
            return UIstatus.Erro("Não existe usuário logado")

            val lojasRef = firebaseStorage
                .getReference( local )
                .child( idLoja )
                .child( nomeImagem )

            lojasRef.putFile( uriImagem ).await()

            val urlImagem = lojasRef.downloadUrl.await().toString()
            return UIstatus.Sucesso( urlImagem )

        }catch (erroUploadImagem: Exception){
            return UIstatus.Erro("Erro ao fazer upload")
        }

    }

}