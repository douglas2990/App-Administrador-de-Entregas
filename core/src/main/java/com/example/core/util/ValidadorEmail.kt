package com.example.core.util


object ValidadorEmail {
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

    fun isValido(email: String): Boolean {
        return email.isNotBlank() && EMAIL_REGEX.matches(email)
    }
}