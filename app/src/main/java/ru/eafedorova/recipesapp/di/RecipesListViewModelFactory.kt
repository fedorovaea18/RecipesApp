package ru.eafedorova.recipesapp.di

import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.ui.recipes.recipesList.RecipesListViewModel

class RecipesListViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<RecipesListViewModel> {

    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }

}
