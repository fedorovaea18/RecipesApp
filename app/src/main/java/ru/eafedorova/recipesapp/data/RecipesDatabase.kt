package ru.eafedorova.recipesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.eafedorova.recipesapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
}
