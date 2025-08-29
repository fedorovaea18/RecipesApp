package ru.eafedorova.recipesapp

import android.app.Application
import ru.eafedorova.recipesapp.di.AppContainer

class RecipeApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }

}
