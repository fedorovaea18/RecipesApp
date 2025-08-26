package ru.eafedorova.recipesapp.di

import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.ui.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<FavoritesViewModel> {

    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }

}
