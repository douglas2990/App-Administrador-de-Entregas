package com.douglas2990.d2990entregasv2.data.remote.firebase.repository.user

import com.douglas2990.d2990entregasv2.model.user.Usuario
import com.example.core.UIstatus

interface IAutenticacaoRepository {
    suspend fun cadastrarUsuario(
        usuario: Usuario,
        uiStatus: (UIstatus<Boolean>)->Unit
    )
    suspend fun logarUsuario(
        usuario: Usuario,
        uiStatus: (UIstatus<Boolean>)->Unit
    )

    suspend fun atualizarUsuario(
        usuario: Usuario,
        uiStatus: (UIstatus<String>) -> Unit
    )

    suspend fun recuperarDadosUsuarioLogado(
        uiStatus: (UIstatus<Usuario>) -> Unit
    )

    suspend fun solicitarAcesso(email: String, uiStatus: (UIstatus<Boolean>) -> Unit)

    fun verificarUsuarioLogado() : Boolean
    fun recuperarIdUsuarioLogado(): String
    fun deslogarUsuario()

    fun ouvirStatusAprovacao(email: String, retornoStatus: (String) -> Unit)
}