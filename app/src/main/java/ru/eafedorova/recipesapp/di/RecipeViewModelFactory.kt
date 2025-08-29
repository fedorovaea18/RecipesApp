package ru.eafedorova.recipesapp.di

import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<RecipeViewModel> {

    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }

}
