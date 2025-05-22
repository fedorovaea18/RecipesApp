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

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.tvTitleCategory.text = category.title
            binding.tvDescriptionCategory.text = category.description
            binding.ivImageCategory.contentDescription = binding.root.context.getString(
                R.string.image_description_category, category.title
            )

            val drawable = try {
                val inputStream: InputStream? =
                    binding.root.context?.assets?.open(category.imageUrl)
                Drawable.createFromStream(inputStream, null)
            } catch (e: IOException) {
                Log.e("CategoriesListAdapter", "Ошибка при загрузке изображения: ${e.message}", e)
                null
            }
            binding.ivImageCategory.setImageDrawable(drawable)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
