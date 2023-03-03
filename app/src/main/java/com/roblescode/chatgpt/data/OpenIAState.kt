package com.roblescode.chatgpt.data

sealed class OpenIAState<out T> {
    data class Success<out T>(val data: T ?) : OpenIAState<T>()
    data class Failure(val error: Exception?) : OpenIAState<Nothing>()
    object Loading : OpenIAState<Nothing>()
}