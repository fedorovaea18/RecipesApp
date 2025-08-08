package ru.eafedorova.recipesapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.eafedorova.recipesapp.Constants.BASE_URL
import ru.eafedorova.recipesapp.model.Category
import ru.eafedorova.recipesapp.model.Recipe

class RecipesRepository {

    private val contentType = "application/json".toMediaType()

    private var retrofit: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType)).build()

    private var service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    suspend fun getCategories(callback: (List<Category>?) -> Unit) {
        withContext(Dispatchers.IO) {
            val categoriesList = try {
                val response = service.getCategories().execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                null
            }
            callback(categoriesList)
        }
    }

    suspend fun getCategoryById(categoryId: Int, callback: (Category?) -> Unit) {
        withContext(Dispatchers.IO) {
            val category = try {
                val response = service.getCategoryById(categoryId).execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                null
            }
            callback(category)
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int, callback: (List<Recipe>?) -> Unit) {
        withContext(Dispatchers.IO) {
            val recipes = try {
                val response = service.getRecipesByCategoryId(categoryId).execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                null
            }
            callback(recipes)
        }
    }


    suspend fun getRecipeById(recipeId: Int, callback: (Recipe?) -> Unit) {
        withContext(Dispatchers.IO) {
            val recipe = try {
                val response = service.getRecipeById(recipeId).execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                null
            }
            callback(recipe)
        }
    }

    suspend fun getRecipesByIds(recipeIds: Set<Int>, callback: (List<Recipe>?) -> Unit) {
        withContext(Dispatchers.IO) {
            val recipes = try {
                val idsString = recipeIds.joinToString(",")
                val response = service.getRecipesByIds(idsString).execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                null
            }
            callback(recipes)
        }
    }

}
