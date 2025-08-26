package ru.eafedorova.recipesapp.ui.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.model.Recipe

class FavoritesViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {
    data class FavoritesState(
        val favoritesList: List<Recipe> = emptyList(),
        val errorResId: Int? = null,
    )

    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    fun loadFavorites() {

        viewModelScope.launch {

            val cachedFavorites = recipesRepository.getFavoritesFromCache()

            if (cachedFavorites.isNotEmpty()) {
                _favoritesState.postValue(
                    FavoritesState(
                        favoritesList = cachedFavorites,
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
