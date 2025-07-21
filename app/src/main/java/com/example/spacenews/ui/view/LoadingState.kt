package com.example.spacenews.ui.view

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LoadingState {
    // Data class para encapsular el estado completo del popup
    data class State(
        val isVisible: Boolean = false,
        val message: String = "Cargando...",
        val isCancelable: Boolean = false
    )

    private val _loadingState = MutableStateFlow(State())
    val loadingState: StateFlow<State> = _loadingState.asStateFlow()

    /**
     * Muestra el popup de carga con un mensaje y opción de ser cancelable.
     * @param message El mensaje a mostrar en el popup.
     * @param isCancelable Si el popup puede ser cerrado al tocar fuera o el botón de atrás.
     */
    fun show(message: String = "Cargando...", isCancelable: Boolean = false) {
        _loadingState.value = State(
            isVisible = true,
            message = message,
            isCancelable = isCancelable
        )
    }

    /**
     * Oculta el popup de carga.
     */
    fun hide() {
        _loadingState.value = State() // Restablece el estado a oculto y valores por defecto
    }
}