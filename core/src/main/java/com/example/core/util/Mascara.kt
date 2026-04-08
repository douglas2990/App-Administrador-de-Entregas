package com.example.core.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object Mascara {
    fun aplicarCnpj(editText: EditText): TextWatcher {
        return object : TextWatcher {
            private var isUpdating = false
            private val mask = "##.###.###/####-##"

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val str = s.toString().replace(Regex("[^\\d]"), "") // Remove tudo que não é número
                var formatado = ""

                if (isUpdating) {
                    isUpdating = false
                    return
                }

                var i = 0
                for (m in mask.toCharArray()) {
                    if (m != '#' && str.length > i) {
                        formatado += m
                        continue
                    }
                    try {
                        formatado += str[i]
                    } catch (e: Exception) {
                        break
                    }
                    i++
                }

                isUpdating = true
                editText.setText(formatado)
                editText.setSelection(formatado.length)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        }
    }
}