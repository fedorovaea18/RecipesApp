package ru.eafedorova.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.eafedorova.recipesapp.databinding.ItemIngredientBinding
import ru.eafedorova.recipesapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    var dataSet: List<Ingredient> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var quantity: Int = 1
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(private val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient, quantity: Int) {
            val totalQuantity = BigDecimal(ingredient.quantity)
                .multiply(BigDecimal(quantity))
                .setScale(1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()

            binding.tvIngredientAmount.text = "$totalQuantity ${ingredient.unitOfMeasure}"
            binding.tvIngredientName.text = ingredient.description

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position], quantity)
    }

    override fun getItemCount() = dataSet.size

}
