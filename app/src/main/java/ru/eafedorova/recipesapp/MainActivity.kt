package ru.eafedorova.recipesapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import ru.eafedorova.recipesapp.databinding.ActivityMainBinding
import ru.eafedorova.recipesapp.model.Category
import ru.eafedorova.recipesapp.model.Recipe
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private val threadPool = Executors.newFixedThreadPool(10)

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d("!!!", "Метод onCreate выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {

            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val request = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string() ?: ""
                val categoriesList = Json.decodeFromString<List<Category>>(responseBody)

                Log.d("!!!", "responseCode: ${response.code}")
                Log.d("!!!", "responseMessage: ${response.message}")
                Log.d("!!!", "Body: $responseBody")
                Log.d("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                Log.d("!!!", "Список категорий: $categoriesList")


                categoriesList.forEach { category ->
                    threadPool.execute {

                        val categoryRecipesRequest = Request.Builder()
                            .url("https://recipes.androidsprint.ru/api/category/${category.id}/recipes")
                            .build()

                        client.newCall(categoryRecipesRequest).execute().use { response ->
                            val categoryRecipesResponse = response.body?.string() ?: ""
                            val categoryRecipesList =
                                Json.decodeFromString<List<Recipe>>(categoryRecipesResponse)

                            Log.d(
                                "!!!",
                                "Рецепты для категории ${category.title}: $categoryRecipesList"
                            )
                        }

                    }
                }
            }
        }
        thread.start()

        binding.btnCategories.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavourites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }

    }

}
