package ru.eafedorova.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.eafedorova.recipesapp.Constants.KEY_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.Constants.PREFS_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.model.Recipe
import java.io.IOException
import java.util.concurrent.Executors

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val portionsCount: Int = 1,
        val recipeImage: Drawable? = null,
        val errorResId: Int? = null,
    )

    private val threadPool = Executors.newFixedThreadPool(10)

    private val recipesRepository = RecipesRepository()

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> get() = _recipeState

    private fun loadDrawableFromAssets(imageUrl: String?): Drawable? {
        return try {
            imageUrl?.let {
                getApplication<Application>().assets.open(it).use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
            }
        } catch (e: IOException) {
            Log.e("RecipeFragment", "Ошибка при загрузке изображения: ${e.message}", e)
            null
        }
    }

    fun loadRecipe(recipeId: Int) {

        threadPool.execute {

            recipesRepository.getRecipeById(recipeId) { recipe ->

                val favoriteSet = getFavorites()
                val isFavorite = favoriteSet.contains(recipeId.toString())

                val drawable = loadDrawableFromAssets(recipe?.imageUrl)

                if (recipe != null) {
                    _recipeState.postValue(
                        RecipeState(
                            recipe = recipe,
                            isFavorite = isFavorite,
                            portionsCount = 1,
                            recipeImage = drawable,
                            errorResId = null,
                        )
                    )
                } else {
                    _recipeState.postValue(
                        RecipeState(
                            recipe = null,
                            isFavorite = false,
                            portionsCount = 1,
                            recipeImage = null,
                            errorResId = R.string.network_error,
                        )
                    )
                }
            }
        }

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
        val currentState = recipeState.value ?: return
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

    fun updatePortionsCount(newCount: Int) {
        val currentState = recipeState.value ?: return
        _recipeState.value = currentState.copy(portionsCount = newCount)
    }
}
