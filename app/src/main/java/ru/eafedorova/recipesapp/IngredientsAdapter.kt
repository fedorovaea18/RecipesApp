package ru.eafedorova.recipesapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.eafedorova.recipesapp.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 1

    class ViewHolder(private val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient, quantity: Int) {
            val totalQuantity = ingredient.quantity.toDouble() * quantity
            binding.tvIngredientAmount.text =
                if (totalQuantity % 1 == 0.0) {
                    "${totalQuantity.toInt()} ${ingredient.unitOfMeasure}"
                } else {
                    "${totalQuantity.toString().format("%.1f")} ${ingredient.unitOfMeasure}"
                }

            binding.tvIngredientName.text = ingredient.description

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
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
