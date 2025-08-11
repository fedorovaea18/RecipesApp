package ru.eafedorova.recipesapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.eafedorova.recipesapp.Constants.BASE_URL
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.model.Category
import ru.eafedorova.recipesapp.model.Recipe

class RecipesRepository {

    private val contentType = "application/json".toMediaType()

    private var retrofit: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType)).build()

    private var service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

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
                ResponseResult.Success(response)
            } catch (e: Exception) {
                ResponseResult.Error(R.string.network_error)
            }
        }
    }

    suspend fun getRecipesByIds(recipeIds: Set<Int>): ResponseResult<List<Recipe>>? {
        return withContext(Dispatchers.IO) {
            try {
                val idsString = recipeIds.joinToString(",")
                val response = service.getRecipesByIds(idsString)
                ResponseResult.Success(response)
            } catch (e: Exception) {
                ResponseResult.Error(R.string.network_error)
            }
        }
    }

}
