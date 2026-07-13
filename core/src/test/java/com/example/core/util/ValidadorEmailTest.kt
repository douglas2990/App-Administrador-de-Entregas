package com.example.core.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidadorEmailTest {

    @Test
    fun isValido_comEmailValido_deveRetornarTrue() {
        assertTrue(ValidadorEmail.isValido("teste@exemplo.com"))
        assertTrue(ValidadorEmail.isValido("usuario.sobrenome@dominio.com.br"))
        assertTrue(ValidadorEmail.isValido("user123@gmail.com"))
    }

    @Test
    fun isValido_comEmailSemArroba_deveRetornarFalse() {
        assertFalse(ValidadorEmail.isValido("testeexemplo.com"))
    }

    @Test
    fun isValido_comEmailSemDominio_deveRetornarFalse() {
        assertFalse(ValidadorEmail.isValido("teste@"))
    }

    @Test
    fun isValido_comEmailSemExtensao_deveRetornarFalse() {
        assertFalse(ValidadorEmail.isValido("teste@dominio"))
    }

    @Test
    fun isValido_comEmailVazio_deveRetornarFalse() {
        assertFalse(ValidadorEmail.isValido(""))
    }

    @Test
    fun isValido_comEspacos_deveRetornarFalse() {
        assertFalse(ValidadorEmail.isValido("teste @exemplo.com"))
    }
}
