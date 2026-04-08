package com.example.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

fun View.esconderTeclado(){

    val inputMethodService = context.getSystemService(
        Context.INPUT_METHOD_SERVICE ) as InputMethodManager

    inputMethodService.hideSoftInputFromWindow(
        windowToken, 0
    )
    //inputMethodService.showSoftInput(this, 0)

}

fun Activity.exibirMensagem( texto: String ){
    Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
}

fun Fragment.exibirMensagem( texto: String ){
    requireActivity().exibirMensagem(texto)
}

fun <T>Activity.navegarPara( destino: Class<T>,finalizar: Boolean = true ){
    startActivity(
        Intent(this, destino)
    )
    if (finalizar) finish()
}