package com.example.core

sealed class UIstatus<out T> {
    object Carregando : UIstatus<Nothing>()
    class Sucesso<T>(val dados: T) : UIstatus<T>()
    class Erro( val erro: String ) : UIstatus<Nothing>()
}