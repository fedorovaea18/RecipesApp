package ru.eafedorova.recipesapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.serialization.json.Json
import ru.eafedorova.recipesapp.databinding.ActivityMainBinding
import ru.eafedorova.recipesapp.model.Category
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d("!!!", "Метод onCreate выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val responseBody = connection.inputStream.bufferedReader().readText()
            val categoriesList = Json.decodeFromString<List<Category>>(responseBody)

            Log.d("!!!", "responseCode: ${connection.responseCode}")
            Log.d("!!!", "responseMessage: ${connection.responseMessage}")
            Log.d("!!!", "Body: $responseBody")
            Log.d("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            Log.d("!!!", "Список категорий: $categoriesList")
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
