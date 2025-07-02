package ru.eafedorova.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.eafedorova.recipesapp.Constants.KEY_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.Constants.PREFS_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.data.STUB
import ru.eafedorova.recipesapp.model.Recipe

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    data class FavoritesState(
        val favoritesList: List<Recipe> = emptyList(),
    )

    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    fun loadFavorites() {
        val favoriteIds = getFavorites().mapNotNull { it.toIntOrNull() }.toSet()
        val favoriteRecipes = STUB.getRecipesByIds(favoriteIds)

        _favoritesState.value = favoritesState.value?.copy(
            favoritesList = favoriteRecipes,
        )
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(
                PREFS_FAVORITE_RECIPES,
                Context.MODE_PRIVATE
            )
        return HashSet(sharedPrefs.getStringSet(KEY_FAVORITE_RECIPES, emptySet()) ?: emptySet())
    }

}
