package ru.eafedorova.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.eafedorova.recipesapp.Constants.KEY_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.Constants.PREFS_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.model.Recipe
import java.util.concurrent.Executors

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    data class FavoritesState(
        val favoritesList: List<Recipe> = emptyList(),
        val errorResId: Int? = null,
    )

    private val threadPool = Executors.newFixedThreadPool(10)

    private val recipesRepository = RecipesRepository()

    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    fun loadFavorites() {

        threadPool.execute {

            val favoriteIds = getFavorites().mapNotNull { it.toIntOrNull() }.toSet()

            recipesRepository.getRecipesByIds(favoriteIds) { favoriteRecipes ->

                if (favoriteRecipes != null) {
                    _favoritesState.postValue(
                        FavoritesState(
                            favoritesList = favoriteRecipes,
                            errorResId = null,
                        )
                    )
                } else {
                    _favoritesState.postValue(
                        FavoritesState(
                            favoritesList = emptyList(),
                            errorResId = R.string.network_error,
                        )
                    )
                }
            }
        }
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
