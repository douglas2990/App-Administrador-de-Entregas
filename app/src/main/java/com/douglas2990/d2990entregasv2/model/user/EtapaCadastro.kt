package com.douglas2990.d2990entregasv2.model.user

enum class EtapaCadastro {
    SOLICITAR_ACESSO, // Apenas e-mail
    AGUARDANDO,       // Mensagem de espera
    COMPLETAR_DADOS   // Libera senha, telefone e nome
}