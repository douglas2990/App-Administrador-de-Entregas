package com.douglas2990.d2990entregasv2.domain.usecase

import com.douglas2990.d2990entregasv2.model.Empresa
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class CadastroEmpresaUseCase {

    fun validarDadosUsuario(empresa: Empresa) : ResultadoValidacao {

        val resultadoValidacao = ResultadoValidacao()
        val valNome = empresa.nome?.validator()
            ?.minLength(6)
            ?.check()

        /*
        val valCNPJ = empresa.cnpj?.validator()
            ?.minLength(14)
            ?.check()

         */





        if (valNome == true)
            resultadoValidacao.nome = true

        /*
        if ( valCNPJ == true )
            resultadoValidacao.cnpj = true

         */


        return resultadoValidacao
    }

}