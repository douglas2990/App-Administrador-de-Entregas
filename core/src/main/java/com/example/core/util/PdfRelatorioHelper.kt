package com.example.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.bumptech.glide.Glide
import com.example.core.model.Rota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




class PdfRelatorioHelper(private val context: Context) {

    suspend fun gerarRelatorio(lista: List<Rota>, nomeEmpresa: String): File? = withContext(Dispatchers.IO) {
        if (lista.isEmpty()) return@withContext null

        val pdfDocument = PdfDocument()
        val paint = Paint()
        val titlePaint = Paint()
        val headerPaint = Paint()

        val pageHeight = 842 // Altura padrão A4
        val pageWidth = 595
        var pageCount = 1

        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageCount).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        var y = 50f

        // Função interna para gerenciar a quebra de página (o "scroll" para a próxima folha)
        fun pularPagina() {
            pdfDocument.finishPage(page)
            pageCount++
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.pages.size + 1).create()
            page = pdfDocument.startPage(pageInfo)
            canvas = page.canvas
            y = 50f

            // Pequeno indicador de continuidade no topo da nova página
            titlePaint.textSize = 10f
            titlePaint.color = Color.GRAY
            canvas.drawText("Continuação: Relatório de Entregas - $nomeEmpresa", 40f, y, titlePaint)
            y += 30f
        }

        // --- Cabeçalho Inicial ---
        titlePaint.textSize = 22f
        titlePaint.isFakeBoldText = true
        titlePaint.color = Color.rgb(44, 62, 80)
        canvas.drawText("RELATÓRIO DE ENTREGAS", 40f, y, titlePaint)
        y += 35f

        headerPaint.textSize = 12f
        canvas.drawText("Motorista: ${lista[0].nomeMotorista}", 40f, y, headerPaint)
        y += 20f
        canvas.drawText("Empresa: ${nomeEmpresa.uppercase()}", 40f, y, headerPaint)
        y += 20f
        val dataHoje = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        canvas.drawText("Emissão: $dataHoje", 40f, y, headerPaint)
        y += 25f
        canvas.drawLine(40f, y, 555f, y, paint.apply { color = Color.LTGRAY; strokeWidth = 2f })
        y += 40f

        // --- Loop das Rotas (O "Adapter" do seu PDF) ---
        lista.forEachIndexed { index, rota ->

            // Verifica se há espaço para os textos básicos. Se não, pula página.
            if (y > 750f) pularPagina()

            paint.color = Color.BLACK
            paint.isFakeBoldText = true
            paint.textSize = 14f
            canvas.drawText("O.S.: #${rota.os.ifBlank { rota.id.takeLast(8) }}", 40f, y, paint)
            y += 22f

            paint.textSize = 12f
            canvas.drawText("${index + 1}. CLIENTE: ${rota.nomeEmpresaDestino.uppercase()}", 40f, y, paint)
            y += 18f

            paint.isFakeBoldText = false
            canvas.drawText("Endereço: ${rota.endereco}", 40f, y, paint)
            y += 25f

            // Tratamento da Imagem (Canhoto)
            if (!rota.comprovanteUrl.isNullOrEmpty()) {
                val bitmap = baixarBitmap(rota.comprovanteUrl.toString())

                if (bitmap != null) {
                    // CRITICAL: Se a imagem (250px) + a posição atual passar do limite, pula página
                    if (y + bitmap.height > 800f) {
                        pularPagina()
                    }

                    canvas.drawBitmap(bitmap, 40f, y, null)
                    y += bitmap.height.toFloat() + 25f
                } else {
                    paint.color = Color.GRAY
                    canvas.drawText("[ERRO AO CARREGAR FOTO]", 40f, y, paint)
                    y += 20f
                }
            } else {
                paint.color = Color.RED
                canvas.drawText("[ENTREGA SEM FOTO]", 40f, y, paint)
                y += 20f
            }

            // Divisória entre itens
            y += 10f
            canvas.drawLine(40f, y, 555f, y, paint.apply { color = Color.rgb(240, 240, 240); strokeWidth = 1f })
            y += 35f
        }

        pdfDocument.finishPage(page)

        val file = File(context.cacheDir, "Relatorio_${System.currentTimeMillis()}.pdf")
        return@withContext try {
            pdfDocument.writeTo(FileOutputStream(file))
            file
        } catch (e: Exception) {
            null
        } finally {
            pdfDocument.close()
        }
    }

    private suspend fun baixarBitmap(url: String): Bitmap? {
        return try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .centerInside()
                .submit(250, 250) // Redimensiona para caber no PDF sem estourar memória
                .get()
        } catch (e: Exception) { null }
    }
}