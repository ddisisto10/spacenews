package com.example.spacenews.ui.Base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacenews.data.NetworkResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel()  {

    // Para manejar el estado de carga
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    // Para enviar eventos de error a la UI
    private val _errorEvent = MutableSharedFlow<NetworkResult<Nothing>>()
    val errorEvent = _errorEvent.asSharedFlow()

    /**
     * Ejecuta un caso de uso y maneja los estados de NetworkResult de forma centralizada.
     *
     * @param T El tipo de dato en caso de éxito.
     * @param useCaseCall La función suspendida que llama al caso de uso.
     * @param onSuccess La acción a realizar con los datos en caso de éxito.
     */
    protected fun <T> executeUseCase(
        useCaseCall: suspend () -> NetworkResult<T>,
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
           // _isLoading.value = true // Inicia el loading

            when (val result = useCaseCall()) {
                is NetworkResult.Success -> {
                    onSuccess(result.data)
                }
                is NetworkResult.Failure -> {
                    _errorEvent.emit(result)
                }
                is NetworkResult.Error -> {
                    _errorEvent.emit(result)
                }
                is NetworkResult.Exception -> {
                    _errorEvent.emit(result)
                }
                is NetworkResult.Loading -> {}
            }

          //  _isLoading.value = false // Finaliza el loading
        }
    }

}