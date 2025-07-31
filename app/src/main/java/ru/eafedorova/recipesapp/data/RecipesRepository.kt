package ru.eafedorova.recipesapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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

    fun getCategories(callback: (List<Category>?) -> Unit) {
        val categoriesList = try {
            val response = service.getCategories().execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
        callback(categoriesList)
    }

    fun getCategoryById(categoryId: Int, callback: (Category?) -> Unit) {
        val category = try {
            val response = service.getCategoryById(categoryId).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
        callback(category)
    }

    fun getRecipesByCategoryId(categoryId: Int, callback: (List<Recipe>?) -> Unit) {
        val recipes = try {
            val response = service.getRecipesByCategoryId(categoryId).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
        callback(recipes)
    }


    fun getRecipeById(recipeId: Int, callback: (Recipe?) -> Unit) {
        val recipe = try {
            val response = service.getRecipeById(recipeId).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
        callback(recipe)
    }

    fun getRecipesByIds(recipeIds: Set<Int>, callback: (List<Recipe>?) -> Unit) {
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
