package ru.eafedorova.recipesapp.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.model.Category
import ru.eafedorova.recipesapp.model.Recipe
import java.io.IOException
import java.util.concurrent.Executors

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeListState(
        val categoryName: String? = null,
        val categoryImage: Drawable? = null,
        val recipesList: List<Recipe> = emptyList(),
        val errorResId: Int? = null,
    )

    private val threadPool = Executors.newFixedThreadPool(10)

    private val recipesRepository = RecipesRepository()

    private val _recipeListState = MutableLiveData(RecipeListState())
    val recipeListState: LiveData<RecipeListState> get() = _recipeListState

    fun loadDrawableFromAssets(imageUrl: String?): Drawable? {
        return try {
            imageUrl?.let {
                getApplication<Application>().assets.open(it).use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
            }
        } catch (e: IOException) {
            Log.e("RecipeListViewModel", "Ошибка при загрузке изображения: ${e.message}", e)
            null
        }
    }

    fun loadRecipeList(category: Category) {

        threadPool.execute {

            val drawable = loadDrawableFromAssets(category.imageUrl)

            recipesRepository.getRecipesByCategoryId(category.id) { recipesList ->

                if (recipesList != null) {
                    _recipeListState.postValue(
                        RecipeListState(
                            categoryName = category.title,
                            categoryImage = drawable,
                            recipesList = recipesList,
                            errorResId = null,
                        )
                    )
                } else {
                    _recipeListState.postValue(
                        RecipeListState(
                            categoryName = category.title,
                            categoryImage = drawable,
                            recipesList = emptyList(),
                            errorResId = R.string.network_error,
                        )
                    )
                }
            }
        }
    }

}
