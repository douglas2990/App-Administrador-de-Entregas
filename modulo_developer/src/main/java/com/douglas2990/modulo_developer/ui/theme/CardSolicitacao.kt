package com.douglas2990.modulo_developer.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.douglas2990.modulo_developer.model.SolicitacaoAcesso

@Composable
fun CardSolicitacao(
    solicitacao: SolicitacaoAcesso,
    onAprovar: () -> Unit,
    onRejeitar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "E-mail: ${solicitacao.email}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Nome: ${solicitacao.nome}", style = MaterialTheme.typography.bodyMedium)

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onRejeitar) {
                    Text("Rejeitar", color = Color.Red)
                }
                Button(onClick = onAprovar, modifier = Modifier.padding(start = 8.dp)) {
                    Text("Aprovar")
                }
            }
        }
    }
}