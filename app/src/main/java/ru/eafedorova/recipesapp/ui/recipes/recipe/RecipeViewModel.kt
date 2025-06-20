package ru.eafedorova.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.eafedorova.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {
    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val portionsCount: Int = 1,
    )

    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState> get() = _recipeState

    init {
        Log.i("!!!", "ViewModel init")
        _recipeState.value = RecipeState(isFavorite = true)
    }

}
