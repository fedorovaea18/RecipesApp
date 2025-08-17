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

data class Category(
    @PrimaryKey val id: Int,
    @ColumnInfo("category_title") val title: String,
    @ColumnInfo("category_description") val description: String,
    @ColumnInfo("category_imageUrl") val imageUrl: String,
) : Parcelable
