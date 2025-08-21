package ru.eafedorova.recipesapp.ui.recipes.recipesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.eafedorova.recipesapp.Constants.IMAGE_URL
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.data.ResponseResult
import ru.eafedorova.recipesapp.model.Category
import ru.eafedorova.recipesapp.model.Recipe
import ru.eafedorova.recipesapp.ui.categories.CategoriesListViewModel.CategoriesListState

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeListState(
        val categoryName: String? = null,
        val categoryImageUrl: String? = null,
        val recipesList: List<Recipe> = emptyList(),
        val errorResId: Int? = null,
    )

    private val recipesRepository = RecipesRepository(application.applicationContext)

    private val _recipeListState = MutableLiveData(RecipeListState())
    val recipeListState: LiveData<RecipeListState> get() = _recipeListState

    fun loadRecipeList(category: Category) {

        val categoryId = category.id

        val drawable = IMAGE_URL + category.imageUrl

        viewModelScope.launch {

            val cachedRecipes = recipesRepository.getRecipesFromCache(categoryId)

            if (cachedRecipes.isNotEmpty()) {
                _recipeListState.postValue(
                    RecipeListState(
                        categoryName = category.title,
                        categoryImageUrl = drawable,
                        recipesList = cachedRecipes,
                        errorResId = null,
                    )
                )
            }

            when (val recipesResult = recipesRepository.getRecipesByCategoryId(categoryId)) {
                is ResponseResult.Success -> {
                    _recipeListState.postValue(
                        RecipeListState(
                            categoryName = category.title,
                            categoryImageUrl = drawable,
                            recipesList = recipesResult.data,
                            errorResId = null,
                        )
                    )
                    recipesRepository.saveRecipes(recipesResult.data)
                }
                is ResponseResult.Error -> {
                    _recipeListState.postValue(
                        RecipeListState(
                            categoryName = category.title,
                            categoryImageUrl = drawable,
                            recipesList = cachedRecipes,
                            errorResId = R.string.network_error,
                        )
                    )
                }
                else -> {
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
