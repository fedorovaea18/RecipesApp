package ru.eafedorova.recipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.eafedorova.recipesapp.data.STUB
import ru.eafedorova.recipesapp.model.Category

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
    )

    private val _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState: LiveData<CategoriesListState> get() = _categoriesListState

    fun loadCategories() {
        val categoriesList = STUB.getCategories()

        _categoriesListState.value = categoriesListState.value?.copy(
            categoriesList = categoriesList,
        )
    }

}
