package ru.eafedorova.recipesapp.di

interface Factory<T> {
    fun create(): T
}
