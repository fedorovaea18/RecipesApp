package ru.eafedorova.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.eafedorova.recipesapp.Constants
import ru.eafedorova.recipesapp.data.RecipeApiService
import ru.eafedorova.recipesapp.data.RecipesDatabase
import ru.eafedorova.recipesapp.data.RecipesRepository

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RecipesDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            RecipesDatabase::
            class.java,
            "database-recipes"
    ).build()

    @Provides
    fun provideRetrofit(): Retrofit {
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.Default.asConverterFactory(contentType))
            .build()
        return retrofit
    }

    @Provides
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }

    @Provides
    fun provideRepository(service: RecipeApiService, db: RecipesDatabase) =
        RecipesRepository(service, db)

}
