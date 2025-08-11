package ru.eafedorova.recipesapp.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.eafedorova.recipesapp.model.Category
import ru.eafedorova.recipesapp.model.Recipe

interface RecipeApiService {

    @GET("recipe/{id}")
    suspend fun getRecipeById(@Path("id") recipeId: Int): Recipe

    @GET("recipes")
    suspend fun getRecipesByIds(@Query("ids") recipeIds: String): List<Recipe>

    @GET("category/{id}")
    suspend fun getCategoryById(@Path("id") categoryId: Int): Category

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategoryId(@Path("id") categoryId: Int): List<Recipe>

    @GET("category")
    suspend fun getCategories(): List<Category>

}
