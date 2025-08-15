package ru.eafedorova.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.eafedorova.recipesapp.model.Category

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategories(categories: List<Category>)

}
