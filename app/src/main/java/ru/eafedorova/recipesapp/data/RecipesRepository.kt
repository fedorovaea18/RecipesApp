package ru.eafedorova.recipesapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.model.Category
import ru.eafedorova.recipesapp.model.Recipe
import javax.inject.Inject

class RecipesRepository @Inject constructor(
    private val service: RecipeApiService,
    private val db: RecipesDatabase
) {

    suspend fun getCategories(): ResponseResult<List<Category>>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getCategories()
                ResponseResult.Success(response)
            } catch (e: Exception) {
                ResponseResult.Error(R.string.network_error)
            }
        }
    }

    suspend fun getCategoryById(categoryId: Int): ResponseResult<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getCategoryById(categoryId)
                ResponseResult.Success(response)
            } catch (e: Exception) {
                ResponseResult.Error(R.string.network_error)
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): ResponseResult<List<Recipe>>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getRecipesByCategoryId(categoryId)
                ResponseResult.Success(response)
            } catch (e: Exception) {
                ResponseResult.Error(R.string.network_error)
            }
        }
    }

    suspend fun getRecipeById(recipeId: Int): ResponseResult<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getRecipeById(recipeId)
                val cachedRecipe = db.recipesDao().getRecipeById(recipeId)
                val updatedRecipe = if (cachedRecipe != null) {
                    response.copy(isFavorite = cachedRecipe.isFavorite)
                } else {
                    response
                }
                db.recipesDao().addRecipe(updatedRecipe)
                ResponseResult.Success(updatedRecipe)
            } catch (e: Exception) {
                ResponseResult.Error(R.string.network_error)
            }
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return db.categoriesDao().getAllCategories()
    }

    suspend fun saveCategories(categories: List<Category>) {
        db.categoriesDao().addCategories(categories)
    }

    suspend fun getRecipesFromCache(categoryId: Int): List<Recipe> {
        return db.recipesDao().getRecipesByCategoryId(categoryId)
    }

    suspend fun saveRecipes(recipes: List<Recipe>) {
        db.recipesDao().addRecipes(recipes)
    }

    suspend fun getFavoritesFromCache(): List<Recipe> {
        return db.recipesDao().getFavoritesRecipes()
    }

    suspend fun setRecipeFavorite(recipeId: Int, isFavorite: Boolean) {
        return db.recipesDao().updateFavoritesStatus(recipeId, isFavorite)
    }

}
