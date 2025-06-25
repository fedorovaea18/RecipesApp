package ru.eafedorova.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.media.Image
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.eafedorova.recipesapp.Constants.KEY_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.Constants.PREFS_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.data.STUB
import ru.eafedorova.recipesapp.model.Recipe
import java.io.IOException
import java.io.InputStream

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val portionsCount: Int = 1,
        val recipeImage: Drawable? = null,
    )

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> get() = _recipeState

    fun loadRecipe(recipeId: Int) {
        //TODO("load from network)
        val recipe = STUB.getRecipeById(recipeId)
        val favoriteSet = getFavorites()
        val isFavorite = favoriteSet.contains(recipeId.toString())

        val drawable = try {
            val inputStream: InputStream? =
                recipe?.let { getApplication<Application>().applicationContext.assets.open(it.imageUrl) }
            Drawable.createFromStream(inputStream, null)
        } catch (e: IOException) {
            Log.e("RecipeFragment", "Ошибка при загрузке изображения: ${e.message}", e)
            null
        }

        _recipeState.value = recipeState.value?.copy(
            recipe = recipe,
            isFavorite = isFavorite,
            portionsCount = 1,
            recipeImage = drawable,
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

}


