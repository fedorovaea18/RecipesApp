package ru.eafedorova.recipesapp.data

sealed class ResponseResult<out R> {
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Error(val exception: Int) : ResponseResult<Nothing>()
}
