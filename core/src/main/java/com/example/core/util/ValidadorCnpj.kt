package com.example.core.util

object ValidadorCnpj {
    fun isValido(cnpj: String): Boolean {
        // 1. Limpa qualquer máscara (garantia extra)
        val numeros = cnpj.replace(Regex("[^\\d]"), "")

        // 2. Valida tamanho e números repetidos
        if (numeros.length != 14) return false
        if (numeros.all { it == numeros[0] }) return false

        // 3. Cálculo dos dígitos verificadores
        return try {
            val calcDigit = { str: String, weights: IntArray ->
                var sum = 0
                for (i in str.indices) sum += (str[i] - '0') * weights[i]
                val rem = sum % 11
                if (rem < 2) '0' else (11 - rem).toString()[0]
            }

            val w1 = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
            val w2 = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

            val d1 = calcDigit(numeros.substring(0, 12), w1)
            val d2 = calcDigit(numeros.substring(0, 12) + d1, w2)

            numeros.endsWith("$d1$d2")
        } catch (e: Exception) {
            false
        }
    }
}

