package com.example.spacenews.ui.Base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.spacenews.data.NetworkResult
import com.example.spacenews.data.PagingError
import com.example.spacenews.ui.view.LoadingState
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar


abstract class BaseFragment<T : BaseViewModel> : Fragment() {

    protected abstract val viewModel: T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.errorEvent.collect { errorResult ->
                        handleNetworkError(errorResult)
                    }
                }

            }
        }
    }

    private fun handleNetworkError(errorResult: NetworkResult<Nothing>) {
        when (errorResult) {
            is NetworkResult.Failure -> {
                Log.e("AppError", "Error ${errorResult.code}: ${errorResult.message}")
                showToast("Error ${errorResult.code}: ${errorResult.message}")
            }
            is NetworkResult.Error -> {
                Log.e("AppError", "Exception: ", errorResult.exception)
                showToast("Error de conexión. Intenta de nuevo.")
            }
            is NetworkResult.Exception -> {
                Log.e("AppError", "Generic Exception: ", errorResult.exception)
                showToast("Ocurrió un error inesperado.")
            }
            else -> {}

        }
    }

    protected fun handlePagingError(error: Throwable) {
        val errorMessage = when (error) {
            is PagingError.Network -> error.message
            is PagingError.Http -> "Error ${error.code}: ${error.message}"
            is PagingError.Unknown -> error.message
            else -> "Ha ocurrido un error inesperado."
        }
        showToast(errorMessage)
    }

    protected fun showToast(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }
}