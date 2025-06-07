package ru.eafedorova.recipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.eafedorova.recipesapp.RecipesListFragment.Companion.ARG_RECIPE
import ru.eafedorova.recipesapp.databinding.FragmentRecipeBinding
import java.io.IOException
import java.io.InputStream

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for RecipeFragment must not be null")

    private var recipe: Recipe? = null

    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION") arguments?.getParcelable(ARG_RECIPE)
        }
        initUI()
        recipe?.let {
            binding.tvTitleRecipeName.text = it.title
            initRecycler(it)
        } ?: run {
            binding.tvTitleRecipeName.text = "Рецепт не найден"

        }
    }

    private fun initRecycler(recipe: Recipe) {
        val dividerItemDecoration = MaterialDividerItemDecoration(
            binding.rvIngredients.context, LinearLayoutManager.VERTICAL
        ).apply {
            setDividerColorResource(binding.rvIngredients.context, R.color.horizontal_border_color)
            setDividerInsetStartResource(binding.rvIngredients.context, R.dimen.medium_space_12)
            setDividerInsetEndResource(binding.rvIngredients.context, R.dimen.medium_space_12)
            isLastItemDecorated = false
        }

        val ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        val methodAdapter = MethodAdapter(recipe.method)
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter
        binding.rvIngredients.addItemDecoration(dividerItemDecoration)
        binding.rvMethod.addItemDecoration(dividerItemDecoration)

        binding.sbPortionCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                ingredientsAdapter.updateIngredients(progress)
                binding.tvPortionCount.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    private fun initUI() {
        binding.tvTitleRecipeName.text = recipe?.title ?: " "

        val drawable = try {
            val inputStream: InputStream? =
                recipe?.let { binding.root.context?.assets?.open(it.imageUrl) }
            Drawable.createFromStream(inputStream, null)
        } catch (e: IOException) {
            Log.e("RecipeFragment", "Ошибка при загрузке изображения: ${e.message}", e)
            null
        }

        binding.ivImageRecipe.setImageDrawable(drawable)

        binding.ibIconHeart.setImageResource(R.drawable.ic_heart_empty)
        binding.ibIconHeart.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                binding.ibIconHeart.setImageResource(R.drawable.ic_heart)
            } else {
                binding.ibIconHeart.setImageResource(R.drawable.ic_heart_empty)
            }
        }

    }

}
