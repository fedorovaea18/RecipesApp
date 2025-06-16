package ru.eafedorova.recipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import ru.eafedorova.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {
    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val portionsCount: Int = 1,
    )

}