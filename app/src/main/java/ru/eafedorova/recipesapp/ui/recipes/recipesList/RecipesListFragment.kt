package ru.eafedorova.recipesapp.ui.recipes.recipesList

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.eafedorova.recipesapp.Constants.ARG_CATEGORY_ID
import ru.eafedorova.recipesapp.Constants.ARG_CATEGORY_IMAGE_URL
import ru.eafedorova.recipesapp.Constants.ARG_CATEGORY_NAME
import ru.eafedorova.recipesapp.Constants.ARG_RECIPE
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.STUB
import ru.eafedorova.recipesapp.databinding.FragmentListRecipesBinding
import ru.eafedorova.recipesapp.ui.recipes.recipe.RecipeFragment
import java.io.IOException
import java.io.InputStream

class RecipesListFragment : Fragment() {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for RecipesListFragment must not be null")

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = requireArguments().getInt(ARG_CATEGORY_ID)
        categoryName = requireArguments().getString(ARG_CATEGORY_NAME)
        categoryImageUrl =
            requireArguments().getString(ARG_CATEGORY_IMAGE_URL)

    }

    private fun initUI() {

        binding.tvTitleCategoryRecipe.text = categoryName

        val drawable = try {
            val inputStream: InputStream? =
                categoryImageUrl?.let { binding.root.context.assets.open(it) }
            Drawable.createFromStream(inputStream, null)
        } catch (e: IOException) {
            Log.e("RecipeListAdapter", "Ошибка при загрузке изображения: ${e.message}", e)
            null
        }

        binding.ivImageCategoryRecipe.setImageDrawable(drawable)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initRecycler()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListRecipesBinding.inflate(inflater)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val recipesAdapter = RecipeListAdapter(STUB.getRecipesByCategoryId(categoryId ?: 0))
        binding.rvRecipes.adapter = recipesAdapter
        recipesAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = bundleOf(ARG_RECIPE to recipe)
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

}
