package com.douglas2990.d2990entregasv2.domain.usecase

data class ResultadoValidacao (
    var nome: Boolean = false,
    var email: Boolean = false,
    var senha: Boolean = false,
    var telefone: Boolean = false,
    var empresa: Boolean = false,
    //var cnpj: Boolean = false
){
    val sucessoValidacaoCadastro: Boolean
        get() = nome && email && senha && telefone && empresa

    val sucessoValidacaoLogin: Boolean
        get() = email && senha
}