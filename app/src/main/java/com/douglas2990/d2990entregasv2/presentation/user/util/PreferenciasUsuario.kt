package com.douglas2990.d2990entregasv2.presentation.user.util

import android.content.Context

object PreferenciasUsuario {
    private const val NOME_ARQUIVO = "dados_cadastro_logistica"
    private const val CHAVE_EMAIL = "email_solicitante"

    private const val CHAVE_NOME = "nome_solicitante"

    // Salva o e-mail
    fun salvarEmail(context: Context, email: String, nome: String) {
        val shared = context.getSharedPreferences(NOME_ARQUIVO, Context.MODE_PRIVATE)
        shared.edit()
            .putString(CHAVE_EMAIL, email)
            .putString(CHAVE_NOME, nome)
            .apply()
    }

    // Recupera o e-mail
    fun recuperarEmail(context: Context): String {
        val shared = context.getSharedPreferences(NOME_ARQUIVO, Context.MODE_PRIVATE)
        return shared.getString(CHAVE_EMAIL, "") ?: ""
    }

    fun recuperarNome(context: Context): String =
        context.getSharedPreferences(NOME_ARQUIVO, Context.MODE_PRIVATE).getString(CHAVE_NOME, "") ?: ""

    // Limpa quando o cadastro terminar (opcional)
    fun limparDados(context: Context) {
        context.getSharedPreferences(NOME_ARQUIVO, Context.MODE_PRIVATE).edit().clear().apply()
    }
}