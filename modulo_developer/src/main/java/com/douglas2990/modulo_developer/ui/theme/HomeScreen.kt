package com.douglas2990.modulo_developer.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.UIstatus

import com.douglas2990.modulo_developer.ui.theme.util.viewmodel.DeveloperViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: DeveloperViewModel = hiltViewModel()) {
    val status by viewModel.solicitacoes.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("D2990 Dev Console") }) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val res = status) {
                is UIstatus.Carregando -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UIstatus.Sucesso -> {
                    val lista = res.dados ?: emptyList()
                    if (lista.isEmpty()) {
                        Text("Nenhuma solicitação pendente", Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn {
                            items(lista) { item ->
                                CardSolicitacao(
                                    solicitacao = item,
                                    onAprovar = { viewModel.aprovarAcesso(item) },
                                    onRejeitar = { viewModel.rejeitarAcesso(item.email) }
                                )
                            }
                        }
                    }
                }
                is UIstatus.Erro -> Text("Erro: ${res.erro}", color = Color.Red)
            }
        }
    }
}