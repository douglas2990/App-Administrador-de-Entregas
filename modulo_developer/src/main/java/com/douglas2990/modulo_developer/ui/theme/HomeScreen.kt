package com.douglas2990.modulo_developer.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.UIstatus
import com.douglas2990.modulo_developer.ui.theme.util.viewmodel.DeveloperViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: DeveloperViewModel = hiltViewModel()) {
    // Coleta o estado da ViewModel (StateFlow)
    val status by viewModel.solicitacoes.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("D2990 Dev Console") }) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val res = status) {
                is UIstatus.Carregando -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is UIstatus.Sucesso -> {
                    val lista = res.dados ?: emptyList()
                    if (lista.isEmpty()) {
                        Text("Nenhuma solicitação pendente", Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            // Uso correto do items para sua lista de SolicitacaoAcesso
                            items(lista) { item ->
                                CardSolicitacao(
                                    solicitacao = item,
                                    onAprovar = { viewModel.aprovarAcesso(item) },
                                    onRejeitar = { viewModel.rejeitarAcesso(item) }
                                )
                            }
                        }
                    }
                }
                is UIstatus.Erro -> {
                    Text("Erro: ${res.erro}", color = Color.Red, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}