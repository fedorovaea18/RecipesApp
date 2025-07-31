package ru.eafedorova.recipesapp.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.eafedorova.recipesapp.model.Category
import ru.eafedorova.recipesapp.model.Recipe

interface RecipeApiService {

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") recipeId: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipesByIds(@Query("ids") recipeIds: String): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") categoryId: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") categoryId: Int): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>

}
