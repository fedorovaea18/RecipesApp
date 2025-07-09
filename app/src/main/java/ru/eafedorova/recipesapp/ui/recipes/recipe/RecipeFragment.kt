package ru.eafedorova.recipesapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for RecipeFragment must not be null")

    private val viewModel: RecipeViewModel by viewModels()

    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter

    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(args.recipeId)
    }

    private fun initAdapters() {
        ingredientsAdapter = IngredientsAdapter()
        methodAdapter = MethodAdapter()

        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter
    }

    private fun initUI(recipeId: Int) {

        viewModel.loadRecipe(recipeId)
        initAdapters()
        setupObserver()
        setupItemDecoration()
        setupSeekBar()
        setIconHeartListener()

    }

    private fun setupObserver() {

        viewModel.recipeState.observe(viewLifecycleOwner) { state ->

            state.recipe?.let { recipe ->
                updateRecipeTitle(recipe.title)
                updateRecipeImage(state.recipeImage)
                updateFavoriteIcon(state.isFavorite)

                binding.tvPortionsCount.text = state.portionsCount.toString()

                ingredientsAdapter.dataSet = recipe.ingredients
                ingredientsAdapter.quantity = state.portionsCount

                methodAdapter.dataSet = recipe.method
            }
        }
    }

    private fun updateRecipeTitle(title: String) {
        binding.tvTitleRecipeName.text = title
    }

    private fun updateRecipeImage(drawable: Drawable?) {
        binding.ivImageRecipe.setImageDrawable(drawable)
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.ibIconHeart.setImageResource(
            if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
        )
    }

    private fun setupItemDecoration() {

        val dividerItemDecoration = MaterialDividerItemDecoration(
            binding.rvIngredients.context, LinearLayoutManager.VERTICAL
        ).apply {
            setDividerColorResource(
                binding.rvIngredients.context,
                R.color.horizontal_border_color
            )
            setDividerInsetStartResource(
                binding.rvIngredients.context,
                R.dimen.medium_space_12
            )
            setDividerInsetEndResource(
                binding.rvIngredients.context,
                R.dimen.medium_space_12
            )
            isLastItemDecorated = false
        }

        binding.rvIngredients.addItemDecoration(dividerItemDecoration)
        binding.rvMethod.addItemDecoration(dividerItemDecoration)
    }

    private fun setupSeekBar() {

        binding.sbPortionCount.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
            viewModel.updatePortionsCount(progress)
        })
    }

    class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser)
                onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    }

    private fun setIconHeartListener() {
        binding.ibIconHeart.setOnClickListener()
        {
            viewModel.onFavoritesClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
