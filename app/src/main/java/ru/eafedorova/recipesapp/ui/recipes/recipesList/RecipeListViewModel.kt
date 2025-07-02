package ru.eafedorova.recipesapp.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.eafedorova.recipesapp.data.STUB
import ru.eafedorova.recipesapp.model.Recipe
import java.io.IOException
import java.io.InputStream

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeListState(
        val categoryName: String? = null,
        val categoryImage: Drawable? = null,
        val recipesList: List<Recipe> = emptyList(),
    )

    private val _recipeListState = MutableLiveData(RecipeListState())
    val recipeListState: LiveData<RecipeListState> get() = _recipeListState

    fun loadRecipeList(categoryId: Int, categoryName: String?, categoryImageUrl: String?) {
        val recipesList = STUB.getRecipesByCategoryId(categoryId)

        val drawable = try {
            val inputStream: InputStream? = categoryImageUrl?.let {
                getApplication<Application>().assets.open(it)
            }
            Drawable.createFromStream(inputStream, null)
        } catch (e: IOException) {
            Log.e("RecipeListFragment", "Ошибка при загрузке изображения: ${e.message}", e)
            null
        }

        _recipeListState.value = recipeListState.value?.copy(
            categoryName = categoryName, categoryImage = drawable, recipesList = recipesList

        )
    }

}
