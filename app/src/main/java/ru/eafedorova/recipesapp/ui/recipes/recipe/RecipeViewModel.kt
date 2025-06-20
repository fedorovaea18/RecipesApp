package ru.eafedorova.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.eafedorova.recipesapp.Constants.KEY_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.Constants.PREFS_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.data.STUB
import ru.eafedorova.recipesapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val portionsCount: Int = 1,
    )

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> get() = _recipeState

    fun loadRecipe(recipeId: Int) {
        //TODO("load from network)
        val recipe = STUB.getRecipeById(recipeId)
        val favoriteSet = getFavorites()
        val isFavorite = favoriteSet.contains(recipeId.toString())
        val currentPortionsCount = _recipeState.value?.portionsCount ?: 1

        _recipeState.value = RecipeState(
            recipe = recipe,
            isFavorite = isFavorite,
            portionsCount = currentPortionsCount,
        )

    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs =
            getApplication<Application>().applicationContext.getSharedPreferences(
                PREFS_FAVORITE_RECIPES,
                Context.MODE_PRIVATE
            )
        return HashSet(sharedPrefs.getStringSet(KEY_FAVORITE_RECIPES, emptySet()) ?: emptySet())
    }

    private fun saveFavorites(recipeIds: Set<String>) {
        val sharedPrefs =
            getApplication<Application>().applicationContext.getSharedPreferences(
                PREFS_FAVORITE_RECIPES,
                Context.MODE_PRIVATE
            )
                ?: return
        with(sharedPrefs.edit()) {
            putStringSet(KEY_FAVORITE_RECIPES, recipeIds)
            apply()
        }
    }

    fun onFavoritesClicked() {
        val currentState = _recipeState.value ?: return
        val recipeId = currentState.recipe?.id.toString()

        val favoriteSet = getFavorites()

        val isFavorite = favoriteSet.contains(recipeId)

        if (isFavorite) {
            favoriteSet.remove(recipeId)
        } else {
            favoriteSet.add(recipeId)
        }
        saveFavorites(favoriteSet)

        _recipeState.value = currentState.copy(isFavorite = !isFavorite)
    }

}


