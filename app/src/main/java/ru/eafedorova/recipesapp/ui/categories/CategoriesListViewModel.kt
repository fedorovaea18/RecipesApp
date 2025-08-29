package ru.eafedorova.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.data.ResponseResult
import ru.eafedorova.recipesapp.model.Category
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val errorResId: Int? = null,
    )

    private val _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState: LiveData<CategoriesListState> get() = _categoriesListState

    fun loadCategories() {

        viewModelScope.launch {

            val cachedCategories = recipesRepository.getCategoriesFromCache()

            if (cachedCategories.isNotEmpty()) {
                _categoriesListState.postValue(
                    CategoriesListState(
                        categoriesList = cachedCategories,
                        errorResId = null
                    )
                )
            }

            when (val categoriesResult = recipesRepository.getCategories()) {
                is ResponseResult.Success -> {
                    _categoriesListState.postValue(
                        CategoriesListState(
                            categoriesList = categoriesResult.data,
                            errorResId = null
                        )
                    )
                    recipesRepository.saveCategories(categoriesResult.data)
                }
                is ResponseResult.Error -> {
                    if (cachedCategories.isNotEmpty()) {
                        _categoriesListState.postValue(
                            CategoriesListState(
                                categoriesList = cachedCategories,
                                errorResId = R.string.network_error
                            )
                        )
                    }
                }
                else -> {
                    _categoriesListState.postValue(
                        CategoriesListState(
                            categoriesList = emptyList(),
                            errorResId = R.string.network_error
                        )
                    )
                }
            }
        }

    }

}
