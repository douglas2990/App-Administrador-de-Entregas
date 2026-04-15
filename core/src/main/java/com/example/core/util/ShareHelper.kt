package com.example.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.core.model.Rota
import java.io.File
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ShareHelper(private val context: Context) {

    /**
     * Envia o PDF diretamente para o WhatsApp ou abre o seletor de apps
     */
    fun compartilharPdf(arquivo: File) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                arquivo
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Segue em anexo o relatório de entregas.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            // Opcional: Tentar abrir direto no WhatsApp
            // intent.setPackage("com.whatsapp")

            context.startActivity(Intent.createChooser(intent, "Enviar Relatório"))
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao compartilhar arquivo", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Gera o texto e abre o WhatsApp com a mensagem pronta
     */
    fun enviarMensagemTexto(lista: List<Rota>) {
        val mensagemFormatada = gerarRelatorioTexto(lista)
        val numero = ConstantesNumeroCelular.WHATSAPP_SUPORTE

        try {
            val url = "https://api.whatsapp.com/send?phone=$numero&text=${URLEncoder.encode(mensagemFormatada, "UTF-8")}"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp não instalado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun gerarRelatorioTexto(lista: List<Rota>): String {
        val dataHoje = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val sb = StringBuilder()

        sb.append("📦 *RELATÓRIO DE ENTREGAS - $dataHoje*\n")
        sb.append("──────────────────────\n\n")

        lista.forEachIndexed { index, rota ->
            sb.append("*${index + 1}. CLIENTE:* ${rota.nomeEmpresaDestino.uppercase()}\n")
            sb.append("🏠 *ENDEREÇO:* ${rota.endereco}\n")
            sb.append("🏁 *STATUS:* ✅ ${rota.status}\n")
            if (!rota.comprovanteUrl.isNullOrBlank()) {
                sb.append("🔗 *FOTO:* ${rota.comprovanteUrl}\n")
            }
            sb.append("──────────────────────\n")
        }

        sb.append("\n_Relatório gerado pelo App Motorista_")
        return sb.toString()
    }
}