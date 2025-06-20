package ru.eafedorova.recipesapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.eafedorova.recipesapp.Constants.ARG_RECIPE_ID
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.databinding.FragmentRecipeBinding
import ru.eafedorova.recipesapp.model.Recipe
import java.io.IOException
import java.io.InputStream

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for RecipeFragment must not be null")

    private val viewModel: RecipeViewModel by viewModels()

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
        val recipeId = arguments?.getInt(ARG_RECIPE_ID) ?: run {
            binding.tvTitleRecipeName.text = "Рецепт не найден"
            return
        }
        initUI()
        viewModel.loadRecipe(recipeId)
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
                binding.tvPortionsCount.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    private fun initUI() {

        viewModel.recipeState.observe(viewLifecycleOwner) { state ->

            state.recipe?.let { recipe ->

                binding.tvTitleRecipeName.text = recipe.title

                val drawable = try {
                    val inputStream: InputStream =
                        state.recipe.let { binding.root.context.assets.open(it.imageUrl) }
                    Drawable.createFromStream(inputStream, null)
                } catch (e: IOException) {
                    Log.e("RecipeFragment", "Ошибка при загрузке изображения: ${e.message}", e)
                    null
                }
                binding.ivImageRecipe.setImageDrawable(drawable)

                binding.ibIconHeart.setImageResource(
                    if (state.isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
                )

                initRecycler(recipe)

            }
        }

        binding.ibIconHeart.setOnClickListener {
            viewModel.onFavoritesClicked()
        }


    }
}
