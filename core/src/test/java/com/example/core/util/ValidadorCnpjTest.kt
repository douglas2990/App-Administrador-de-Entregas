package com.example.core.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidadorCnpjTest {

    @Test
    fun isValido_comCnpjValidoComMascara_deveRetornarTrue() {
        val cnpjValido = "11.222.333/0001-81"
        assertTrue(ValidadorCnpj.isValido(cnpjValido))
    }

    @Test
    fun isValido_comCnpjValidoSemMascara_deveRetornarTrue() {
        val cnpjValido = "11222333000181"
        assertTrue(ValidadorCnpj.isValido(cnpjValido))
    }

    @Test
    fun isValido_comCnpjInvalido_deveRetornarFalse() {
        val cnpjInvalido = "11.222.333/0001-00"
        assertFalse(ValidadorCnpj.isValido(cnpjInvalido))
    }

    @Test
    fun isValido_comCnpjCurto_deveRetornarFalse() {
        val cnpjCurto = "11.222.333/0001"
        assertFalse(ValidadorCnpj.isValido(cnpjCurto))
    }

    @Test
    fun isValido_comCnpjLongo_deveRetornarFalse() {
        val cnpjLongo = "11.222.333/0001-811"
        assertFalse(ValidadorCnpj.isValido(cnpjLongo))
    }

    @Test
    fun isValido_comCnpjDigitosRepetidos_deveRetornarFalse() {
        val cnpjRepetido = "11111111111111"
        assertFalse(ValidadorCnpj.isValido(cnpjRepetido))
    }

    @Test
    fun isValido_comStringVazia_deveRetornarFalse() {
        assertFalse(ValidadorCnpj.isValido(""))
    }
}
