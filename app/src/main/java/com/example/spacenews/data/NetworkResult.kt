package com.example.spacenews.data

sealed class NetworkResult<out R> {

    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Failure(val code: Int, val message: String): NetworkResult<Nothing>()
    data class Error(val exception: java.lang.Exception?) : NetworkResult<Nothing>()

    data class Exception(val exception: java.lang.Exception?): NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Exception -> "Exception[error=$exception]"
            is Failure -> "Failure[error=$message]"
            Loading -> "Loading"
        }
    }
}




/**
 * `true` if [NetworkResult] is of type [Success] & holds non-null [Success.data].
 */
val NetworkResult<*>.succeeded
    get() = this is NetworkResult.Success && data != null
