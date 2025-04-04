package ru.eafedorova.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.eafedorova.recipesapp.databinding.ItemCategoryBinding
import java.io.IOException
import java.io.InputStream

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.tvTitleCategory.text = category.title
            binding.tvDescriptionCategory.text = category.description
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.tvTitleCategory.text = dataSet[position].title
        viewHolder.binding.tvDescriptionCategory.text = dataSet[position].description
        val drawable =
            try {
                val inputStream: InputStream? =
                    viewHolder.itemView.context?.assets?.open(dataSet[position].imageUrl)
                Drawable.createFromStream(inputStream, null)
            } catch (e: IOException) {
                Log.e("CategoriesListAdapter", "Ошибка при загрузке изображения: ${e.message}", e)
                null
            }
        viewHolder.binding.ivImageCategory.setImageDrawable(drawable)

    }

    override fun getItemCount() = dataSet.size

}
