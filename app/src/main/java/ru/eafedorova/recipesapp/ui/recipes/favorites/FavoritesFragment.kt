package ru.eafedorova.recipesapp.ui.recipes.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.eafedorova.recipesapp.Constants.ARG_RECIPE_ID
import ru.eafedorova.recipesapp.Constants.KEY_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.Constants.PREFS_FAVORITE_RECIPES
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.STUB
import ru.eafedorova.recipesapp.databinding.FragmentFavoritesBinding
import ru.eafedorova.recipesapp.ui.recipes.recipe.RecipeFragment
import ru.eafedorova.recipesapp.ui.recipes.recipesList.RecipeListAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for FavoritesFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs =
            requireContext().getSharedPreferences(PREFS_FAVORITE_RECIPES, Context.MODE_PRIVATE)
        return HashSet(sharedPrefs.getStringSet(KEY_FAVORITE_RECIPES, emptySet()) ?: emptySet())
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(ARG_RECIPE_ID to recipeId)
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    private fun initRecycler() {
        val favoriteIds = getFavorites().mapNotNull { it.toIntOrNull() }.toSet()
        val favoriteRecipes = STUB.getRecipesByIds(favoriteIds)
        val favoriteListAdapter = RecipeListAdapter(favoriteRecipes)

        if (favoriteRecipes.isEmpty()) {
            binding.rvFavorites.visibility = View.GONE
            binding.tvTitleFavoritesEmpty.visibility = View.VISIBLE
        } else {
            binding.rvFavorites.visibility = View.VISIBLE
            binding.tvTitleFavoritesEmpty.visibility = View.GONE
        }

        binding.rvFavorites.adapter = favoriteListAdapter
        favoriteListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

}
