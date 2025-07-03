package ru.eafedorova.recipesapp.ui.recipes.recipesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import ru.eafedorova.recipesapp.Constants.ARG_CATEGORY_ID
import ru.eafedorova.recipesapp.Constants.ARG_CATEGORY_IMAGE_URL
import ru.eafedorova.recipesapp.Constants.ARG_CATEGORY_NAME
import ru.eafedorova.recipesapp.Constants.ARG_RECIPE_ID
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.databinding.FragmentListRecipesBinding
import ru.eafedorova.recipesapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for RecipesListFragment must not be null")

    private val viewModel: RecipeListViewModel by viewModels()

    private lateinit var recipeListAdapter: RecipeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListRecipesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryId = arguments?.getInt(ARG_CATEGORY_ID) ?: 0
        val categoryName = arguments?.getString(ARG_CATEGORY_NAME)
        val categoryImageUrl =
            arguments?.getString(ARG_CATEGORY_IMAGE_URL)

        initUI(categoryId, categoryName, categoryImageUrl)
    }

    private fun initUI(categoryId: Int, categoryName: String?, categoryImageUrl: String?) {
        viewModel.loadRecipeList(categoryId, categoryName, categoryImageUrl)
        initAdapters()
        setupObserver()
    }

    private fun initAdapters() {
        recipeListAdapter = RecipeListAdapter(emptyList())
        binding.rvRecipes.adapter = recipeListAdapter

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun setupObserver() {

        viewModel.recipeListState.observe(viewLifecycleOwner) { state ->
            binding.tvTitleCategoryRecipe.text = state.categoryName
            binding.ivImageCategoryRecipe.setImageDrawable(state.categoryImage)

            recipeListAdapter.updateRecipes(state.recipesList)
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(ARG_RECIPE_ID to recipeId)
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}
