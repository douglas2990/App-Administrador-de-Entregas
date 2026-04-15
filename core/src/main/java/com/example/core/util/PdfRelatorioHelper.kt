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

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        var y = 50f

        // --- CABEÇALHO ---
        titlePaint.textSize = 22f
        titlePaint.isFakeBoldText = true
        titlePaint.color = Color.rgb(44, 62, 80)
        canvas.drawText("RELATÓRIO DE ENTREGAS", 40f, y, titlePaint)
        y += 35f

        headerPaint.textSize = 12f
        headerPaint.isAntiAlias = true
        canvas.drawText("Motorista: ${lista[0].nomeMotorista}", 40f, y, headerPaint)
        y += 20f

        headerPaint.isFakeBoldText = true
        canvas.drawText("Empresa: ${nomeEmpresa.uppercase()}", 40f, y, headerPaint)
        headerPaint.isFakeBoldText = false
        y += 20f

        val dataHoje = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        canvas.drawText("Emissão: $dataHoje", 40f, y, headerPaint)
        y += 25f

        canvas.drawLine(40f, y, 555f, y, paint.apply { color = Color.LTGRAY; strokeWidth = 2f })
        y += 40f

        // --- CONTEÚDO ---
        lista.forEachIndexed { index, rota ->
            if (y > 700f) {
                pdfDocument.finishPage(page)
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                y = 50f
            }

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
            y += 20f

            if (!rota.comprovanteUrl.isNullOrEmpty()) {
                val bitmap = baixarBitmap(rota.comprovanteUrl.toString())
                bitmap?.let {
                    canvas.drawBitmap(it, 40f, y, paint)
                    y += 130f
                }
            } else {
                paint.color = Color.RED
                canvas.drawText("[ENTREGA SEM FOTO]", 40f, y, paint)
                y += 20f
            }
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
            Glide.with(context).asBitmap().load(url).submit(200, 200).get()
        } catch (e: Exception) { null }
    }
}