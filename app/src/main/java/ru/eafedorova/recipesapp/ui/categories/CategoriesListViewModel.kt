package ru.eafedorova.recipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.model.Category

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val errorResId: Int? = null,
    )

    private val recipesRepository = RecipesRepository()

    private val _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState: LiveData<CategoriesListState> get() = _categoriesListState

    fun loadCategories() {

        viewModelScope.launch {

            recipesRepository.getCategories { categoriesList ->

                if (categoriesList != null) {
                    _categoriesListState.postValue(
                        CategoriesListState(
                            categoriesList = categoriesList,
                            errorResId = null,
                        )
                    )
                } else {
                    _categoriesListState.postValue(
                        CategoriesListState(
                            categoriesList = emptyList(),
                            errorResId = R.string.network_error,
                        )
                    )
                }

            }
        }
    }

}
