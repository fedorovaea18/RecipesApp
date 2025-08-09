package ru.eafedorova.recipesapp.ui.recipes.recipesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.eafedorova.recipesapp.Constants.IMAGE_URL
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.model.Category
import ru.eafedorova.recipesapp.model.Recipe
import java.util.concurrent.Executors

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeListState(
        val categoryName: String? = null,
        val categoryImageUrl: String? = null,
        val recipesList: List<Recipe> = emptyList(),
        val errorResId: Int? = null,
    )

    private val threadPool = Executors.newFixedThreadPool(10)

    private val recipesRepository = RecipesRepository()

    private val _recipeListState = MutableLiveData(RecipeListState())
    val recipeListState: LiveData<RecipeListState> get() = _recipeListState

    fun loadRecipeList(category: Category) {

        threadPool.execute {

            val drawable = IMAGE_URL + category.imageUrl

            recipesRepository.getRecipesByCategoryId(category.id) { recipesList ->

                if (recipesList != null) {
                    _recipeListState.postValue(
                        RecipeListState(
                            categoryName = category.title,
                            categoryImageUrl = drawable,
                            recipesList = recipesList,
                            errorResId = null,
                        )
                    )
                } else {
                    _recipeListState.postValue(
                        RecipeListState(
                            categoryName = category.title,
                            categoryImageUrl = drawable,
                            recipesList = emptyList(),
                            errorResId = R.string.network_error,
                        )
                    )
                }
            }
        }
    }

}
