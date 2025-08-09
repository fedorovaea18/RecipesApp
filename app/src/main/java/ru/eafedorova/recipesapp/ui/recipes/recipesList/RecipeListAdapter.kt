package ru.eafedorova.recipesapp.ui.recipes.recipesList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.eafedorova.recipesapp.Constants
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.databinding.ItemRecipeBinding
import ru.eafedorova.recipesapp.model.Recipe

class RecipeListAdapter(private var dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.tvTitleRecipeName.text = recipe.title
            binding.ivImageRecipe.contentDescription = binding.root.context.getString(
                R.string.image_description_category, recipe.title
            )

            Glide
                .with(binding.root)
                .load(Constants.IMAGE_URL + recipe.imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivImageRecipe)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])

        viewHolder.binding.root.setOnClickListener {
            itemClickListener?.onItemClick(dataSet[position].id)
        }
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecipes(dataSet: List<Recipe>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

}
