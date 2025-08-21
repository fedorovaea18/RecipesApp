package ru.eafedorova.recipesapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.model.Recipe

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    data class FavoritesState(
        val favoritesList: List<Recipe> = emptyList(),
        val errorResId: Int? = null,
    )

    private val recipesRepository = RecipesRepository(application.applicationContext)

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
