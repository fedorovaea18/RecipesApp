package ru.eafedorova.recipesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.eafedorova.recipesapp.model.Category
import ru.eafedorova.recipesapp.model.Recipe

@Database(entities = [Category::class, Recipe::class], version = 3)
@TypeConverters(Converters::class)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}
