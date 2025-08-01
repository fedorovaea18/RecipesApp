package ru.eafedorova.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.eafedorova.recipesapp.databinding.FragmentFavoritesBinding
import ru.eafedorova.recipesapp.ui.recipes.recipesList.RecipeListAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for FavoritesFragment must not be null")

    private val viewModel: FavoritesViewModel by viewModels()

    private lateinit var favoriteListAdapter: RecipeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        viewModel.loadFavorites()
        initAdapters()
        setupObserver()
    }

    private fun initAdapters() {
        favoriteListAdapter = RecipeListAdapter(emptyList())
        binding.rvFavorites.adapter = favoriteListAdapter

        favoriteListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun setupObserver() {

        viewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            favoriteListAdapter.updateRecipes(state.favoritesList)

            if (state.favoritesList.isEmpty()) {
                binding.rvFavorites.visibility = View.GONE
                binding.tvTitleFavoritesEmpty.visibility = View.VISIBLE
            } else {
                binding.rvFavorites.visibility = View.VISIBLE
                binding.tvTitleFavoritesEmpty.visibility = View.GONE
            }
        }

    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(
                recipeId
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
