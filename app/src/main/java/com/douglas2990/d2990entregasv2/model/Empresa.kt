package com.douglas2990.d2990entregasv2.model

data class Empresa (
    var id: String = "",
    val nome : String = "",
    val email: String = "",
    val cnpj: String = "",
    val telefone: String = ""
){
    fun toMap() : Map<String, Any> {
        val dados = mutableMapOf<String, Any>()

        if( id.isNotEmpty() ) dados["id"] = id
        //dados["idCategoria"]    = id
        dados["nome"]           = nome
        dados["email"]          = email
        dados["cnpj"]           = cnpj
        dados["telefone"]       = telefone

        return dados
    }

}