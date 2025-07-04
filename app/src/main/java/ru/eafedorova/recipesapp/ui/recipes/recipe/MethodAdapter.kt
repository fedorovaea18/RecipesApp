package ru.eafedorova.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.eafedorova.recipesapp.databinding.ItemMethodBinding

class MethodAdapter :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    var dataSet: List<String> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(private val binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stepNumber: Int, methodStep: String) {
            binding.tvMethod.text = "$stepNumber. $methodStep"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(position + 1, dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}
