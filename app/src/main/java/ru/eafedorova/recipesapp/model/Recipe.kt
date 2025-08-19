package ru.eafedorova.recipesapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity
@Parcelize
@Serializable

data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo("recipe_title") val title: String,
    @ColumnInfo("recipe_ingredients") val ingredients: List<Ingredient>,
    @ColumnInfo("recipe_method") val method: List<String>,
    @ColumnInfo("recipe_imageUrl") val imageUrl: String,
    @ColumnInfo ("recipe_categoryId") val categoryId: Int? = null,
) : Parcelable
