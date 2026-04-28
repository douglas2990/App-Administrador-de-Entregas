package com.douglas2990.d2990entregasv2.domain.usecase

import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.user.IAutenticacaoRepository
import com.douglas2990.d2990entregasv2.model.user.Usuario
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import javax.inject.Inject

class AutenticacaoUseCase @Inject constructor(
    private val repository: IAutenticacaoRepository // Ensure the repository is also injectable via a Module
) {

    fun validarCadastroUsuario(usuario: Usuario) : ResultadoValidacao {

        val resultadoValidacao = ResultadoValidacao()
        val valNome = usuario.nome.validator()
            .minLength(6)
            .check()

        val valEmail = usuario.email.validator()
            .validEmail()
            .check()

        val valSenha = usuario.senha.validator()
            .minLength(6)
            .check()

/*        val valTelefone = usuario.telefone.validator()
            .minLength(14)
            .check() */
/*
        val valTelefone = usuario.telefone.validator()
            .minLength(11)
            .check()*/

        val valTelefone = usuario.telefone.validator()
            .nonEmpty() // Garante que não está vazio
            .minLength(10) // Aceita fixo (10) ou celular (11)
            .maxLength(11) // Garante que não venha lixo
            .check()

        if ( valNome )
            resultadoValidacao.nome = true

        if ( valEmail )
            resultadoValidacao.email = true

        if ( valSenha )
            resultadoValidacao.senha = true

        if ( valTelefone )
            resultadoValidacao.telefone = true

        return resultadoValidacao
    }

    fun validarLoginUsuario(usuario: Usuario) : ResultadoValidacao {

        val resultadoValidacao = ResultadoValidacao()


        val valEmail = usuario.email.validator()
            .validEmail()
            .check()

        val valSenha = usuario.senha.validator()
            .minLength(6)
            .check()

        if ( valEmail )
            resultadoValidacao.email = true

        if ( valSenha )
            resultadoValidacao.senha = true


        return resultadoValidacao
    }

    // Dentro do seu AutenticacaoUseCase.kt
    fun validarSolicitacaoAcesso(nome: String, email: String): ResultadoValidacao {
        val resultado = ResultadoValidacao()

        val valNome = nome.validator().minLength(6).check()
        val valEmail = email.validator().validEmail().check()

        if (valNome) resultado.nome = true
        if (valEmail) resultado.email = true

        return resultado
    }
}