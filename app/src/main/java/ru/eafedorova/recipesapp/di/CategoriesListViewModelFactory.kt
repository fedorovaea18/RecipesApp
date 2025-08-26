package ru.eafedorova.recipesapp.di

import ru.eafedorova.recipesapp.data.RecipesRepository
import ru.eafedorova.recipesapp.ui.categories.CategoriesListViewModel

class CategoriesListViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<CategoriesListViewModel> {

    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }

}
