package ru.eafedorova.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.eafedorova.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM Recipe WHERE recipe_categoryId = :categoryId")
    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipes(recipes: List<Recipe>)

    @Query("SELECT * FROM Recipe WHERE recipe_isFavorite = 1")
    suspend fun getFavoritesRecipes(): List<Recipe>

    @Query("UPDATE Recipe SET recipe_isFavorite = :isFavorite WHERE id = :recipeId")
    suspend fun updateFavoritesStatus(recipeId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM Recipe WHERE id = :recipeId LIMIT 1")
    suspend fun getRecipeById(recipeId: Int): Recipe?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipe(recipe: Recipe)

}
