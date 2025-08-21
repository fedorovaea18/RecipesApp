package ru.eafedorova.recipesapp.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import ru.eafedorova.recipesapp.model.Ingredient

class Converters {

    @TypeConverter
    fun convertFromIngredientsList(ingredients: List<Ingredient>): String{
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun convertToIngredientsList(ingredients: String): List<Ingredient>{
        return Json.decodeFromString(ingredients)
    }

    @TypeConverter
    fun convertFromMethodList(method: List<String>): String{
        return Json.encodeToString(method)
    }

    @TypeConverter
    fun convertToMethodList(method: String): List<String>{
        return Json.decodeFromString(method)
    }

}