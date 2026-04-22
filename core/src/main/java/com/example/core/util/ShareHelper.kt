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
     * Envia o PDF para o WhatsApp do Administrador específico
     */
    fun compartilharPdf(arquivo: File, telefone: String) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                arquivo
            )

            // Limpa e garante o DDI 55
            val numeroLimpo = telefone.replace(Regex("[^0-9]"), "")
            val numeroDestino = if (numeroLimpo.startsWith("55")) numeroLimpo else "55$numeroLimpo"

            // Usamos ACTION_SEND com o pacote do WhatsApp para enviar o arquivo DIRETO
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                // O segredo para ir para o número certo com arquivo é o "jid"
                putExtra("jid", "$numeroDestino@s.whatsapp.net")
                setPackage("com.whatsapp") // Tenta abrir o WhatsApp normal
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(intent)
        } catch (e: Exception) {
            // Se falhar o setPackage (Wp não instalado), abre o seletor padrão
            abrirSeletorPadrao(arquivo)
        }
    }

    private fun abrirSeletorPadrao(arquivo: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", arquivo)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Enviar Relatório"))
    }

    /**
     * Abre o WhatsApp com o texto formatado para o número do Admin
     */
    fun enviarMensagemTexto(lista: List<Rota>, telefone: String) {
        val mensagemFormatada = gerarRelatorioTexto(lista)

        // Limpa e garante o DDI 55
        val numeroLimpo = telefone.replace(Regex("[^0-9]"), "")
        val numeroDestino = if (numeroLimpo.startsWith("55")) numeroLimpo else "55$numeroLimpo"

        try {
            val url = "https://api.whatsapp.com/send?phone=$numeroDestino&text=${URLEncoder.encode(mensagemFormatada, "UTF-8")}"
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