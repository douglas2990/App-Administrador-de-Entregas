package com.douglas2990.modulo_developer.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.douglas2990.modulo_developer.ui.theme.util.viewmodel.SolicitacoesViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TelaAprovacao(viewModel: SolicitacoesViewModel = hiltViewModel()
) {
    val lista by viewModel.listaSolicitacoes // Observa a lista da ViewModel

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Pedidos de Acesso",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            items(lista) { solicitacao ->
                // Chamando o Card que você já criou!
                CardSolicitacao(
                    solicitacao = solicitacao,
                    onAprovar = { viewModel.aprovar(solicitacao) },
                    onRejeitar = { viewModel.rejeitar(solicitacao) }
                )
            }
        }
    }
}