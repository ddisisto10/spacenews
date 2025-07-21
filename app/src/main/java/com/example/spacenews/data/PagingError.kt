package com.example.spacenews.data

sealed class PagingError : Throwable() {
    data class Network(override val message: String = "Error de conexión. Revisa tu internet.") : PagingError()
    data class Http(val code: Int, override val message: String) : PagingError()
    data class Unknown(override val message: String = "Ocurrió un error inesperado.") : PagingError()
}