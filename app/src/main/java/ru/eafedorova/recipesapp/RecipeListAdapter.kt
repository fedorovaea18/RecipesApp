package ru.eafedorova.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.eafedorova.recipesapp.databinding.ItemRecipeBinding
import java.io.IOException
import java.io.InputStream

class RecipeListAdapter(private val dataSet: List<Recipe>) :
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

            val drawable = try {
                val inputStream: InputStream? =
                    binding.root.context?.assets?.open(recipe.imageUrl)
                Drawable.createFromStream(inputStream, null)
            } catch (e: IOException) {
                Log.e("CategoriesListAdapter", "Ошибка при загрузке изображения: ${e.message}", e)
                null
            }
            binding.ivImageRecipe.setImageDrawable(drawable)
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

}
