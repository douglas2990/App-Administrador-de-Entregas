package com.example.core.repository

import android.net.Uri

object ImageHelper {
    fun prepararParaUpload(context: android.content.Context, uri: Uri): ByteArray? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val originalBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)

            // 1. Definir limite 720p (1280px no lado maior)
            val limite = 1280f
            val largura = originalBitmap.width.toFloat()
            val altura = originalBitmap.height.toFloat()

            val scale = if (largura > altura) limite / largura else limite / altura

            // 2. Redimensionar apenas se for maior que 720p
            val bitmapRedimensionado = if (scale < 1) {
                android.graphics.Bitmap.createScaledBitmap(
                    originalBitmap,
                    (largura * scale).toInt(),
                    (altura * scale).toInt(),
                    true
                )
            } else {
                originalBitmap
            }

            // 3. Comprimir para JPEG com 70% de qualidade
            val outputStream = java.io.ByteArrayOutputStream()
            bitmapRedimensionado.compress(android.graphics.Bitmap.CompressFormat.JPEG, 70, outputStream)

            outputStream.toByteArray()
        } catch (e: Exception) {
            null
        }
    }
}