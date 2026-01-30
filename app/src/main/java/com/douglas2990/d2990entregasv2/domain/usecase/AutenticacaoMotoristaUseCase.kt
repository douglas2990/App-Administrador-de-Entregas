package com.douglas2990.d2990entregasv2.domain.usecase

import com.douglas2990.d2990entregasv2.model.Motorista
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class AutenticacaoMotoristaUseCase {

    fun validarDadosUsuario(motorista: Motorista) : ResultadoValidacao {

        val resultadoValidacao = ResultadoValidacao()
        val valNome = motorista.nome.validator()
            .minLength(6)
            .check()

        val valEmail = motorista.email.validator()
            .validEmail()
            .check()

        val valSenha = motorista.senha.validator()
            .minLength(6)
            .check()

        val valEmpresa = motorista.empresa.validator()
            .minLength(6)
            .check()

        val valTelefone = motorista.telefone.validator()
            .minLength(14)
            .check()

        if (valNome == true)
            resultadoValidacao.nome = true

        if (valEmail == true)
        //valEmail == true
            resultadoValidacao.email = true

        if (valSenha == true)
            resultadoValidacao.senha = true

        if (valTelefone == true)
            resultadoValidacao.telefone = true

        if (valEmpresa == true)
            resultadoValidacao.empresa = true

        return resultadoValidacao
    }

    fun validarLoginEmpresa(usuario: Motorista) : ResultadoValidacao {

        val resultadoValidacao = ResultadoValidacao()


        val valEmail = usuario.email.validator()
            .validEmail()
            .check()

        val valSenha = usuario.senha.validator()
            .minLength(6)
            .check()

        if ( valEmail == true )
            resultadoValidacao.email = true

        if ( valSenha == true )
            resultadoValidacao.senha = true


        return resultadoValidacao
    }

    fun validarCadastroUsuario(usuario: Motorista) : ResultadoValidacao {

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

        val valTelefone = usuario.telefone.validator()
            .minLength(14)
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

}