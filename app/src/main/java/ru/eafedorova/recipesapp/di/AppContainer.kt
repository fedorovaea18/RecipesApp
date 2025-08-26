package ru.eafedorova.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.eafedorova.recipesapp.Constants
import ru.eafedorova.recipesapp.data.RecipeApiService
import ru.eafedorova.recipesapp.data.RecipesDatabase
import ru.eafedorova.recipesapp.data.RecipesRepository

class AppContainer(context: Context) {

    private val contentType = "application/json".toMediaType()

    private var retrofit: Retrofit =
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.Default.asConverterFactory(contentType)).build()

    private var service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val db = Room.databaseBuilder(
        context.applicationContext,
        RecipesDatabase::class.java,
        "database-recipes"
    ).build()

    val repository = RecipesRepository(service, db)

    val categoriesListViewModel = CategoriesListViewModelFactory(repository)
    val recipesListViewModel = RecipesListViewModelFactory(repository)
    val favoritesViewModel = FavoritesViewModelFactory(repository)
    val recipeViewModel = RecipeViewModelFactory(repository)

}
