package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import com.douglas2990.d2990entregasv2.model.Empresa
import com.example.core.UIstatus

interface IEmpresaRepository {

    suspend fun salvar(
        empresa: Empresa,
        uIstatus: (UIstatus<String>) -> Unit
    )

    suspend fun atualizar(
        empresa: Empresa,
        uIstatus: (UIstatus<String>) -> Unit
    )

    suspend fun listar(
        uiStatus: (UIstatus<List<Empresa>>) -> Unit
    )

    suspend fun recuperarEmpresaPeloId(
        idProduto: String,
        uiStatus: (UIstatus<Empresa>) -> Unit
    )

    suspend fun remover(
        idProduto: String,
        uiStatus: (UIstatus<Boolean>) -> Unit
    )

    suspend fun verificarCnpjExistente(cnpj: String, idEmpresaAtual: String? = null): Boolean

}