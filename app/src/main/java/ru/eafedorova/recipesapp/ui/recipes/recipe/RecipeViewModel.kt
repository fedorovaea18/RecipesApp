package ru.eafedorova.recipesapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.eafedorova.recipesapp.Constants.IMAGE_URL
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.data.ResponseResult
import ru.eafedorova.recipesapp.model.Recipe
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val portionsCount: Int = 1,
        val recipeImageUrl: String? = null,
        val errorResId: Int? = null,
    )

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> get() = _recipeState

    fun loadRecipe(recipeId: Int) {

        viewModelScope.launch {

            when (val recipeResult = recipesRepository.getRecipeById(recipeId)) {
                is ResponseResult.Success -> {
                    val recipe = recipeResult.data
                    val drawable = IMAGE_URL + recipeResult.data.imageUrl
                    _recipeState.postValue(
                        RecipeState(
                            recipe = recipe,
                            isFavorite = recipe.isFavorite,
                            portionsCount = 1,
                            recipeImageUrl = drawable,
                            errorResId = null,
                        )
                    )
                }
                else -> {
                    _recipeState.postValue(
                        RecipeState(
                            recipe = null,
                            isFavorite = false,
                            portionsCount = 1,
                            recipeImageUrl = null,
                            errorResId = R.string.network_error,
                        )
                    )
                }
            }

        }

    }

    fun onFavoritesClicked() {
        val currentState = recipeState.value ?: return
        val recipeId = currentState.recipe?.id ?: return

        val newFavoritesStatus = !currentState.isFavorite

        viewModelScope.launch {

            recipesRepository.setRecipeFavorite(recipeId, newFavoritesStatus)
            _recipeState.postValue(currentState.copy(isFavorite = newFavoritesStatus))

        }
    }

    fun updatePortionsCount(newCount: Int) {
        val currentState = recipeState.value ?: return
        _recipeState.value = currentState.copy(portionsCount = newCount)
    }
}
