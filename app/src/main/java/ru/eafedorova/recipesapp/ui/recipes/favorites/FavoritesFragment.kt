package ru.eafedorova.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.RecipeApplication
import ru.eafedorova.recipesapp.databinding.FragmentFavoritesBinding
import ru.eafedorova.recipesapp.ui.recipes.recipesList.RecipesListAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for FavoritesFragment must not be null")

    private lateinit var favoritesViewModel: FavoritesViewModel

    private lateinit var favoriteListAdapter: RecipesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireContext().applicationContext as RecipeApplication).appContainer
        favoritesViewModel = appContainer.favoritesViewModel.create()
    }

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
        favoritesViewModel.loadFavorites()
        initAdapters()
        setupObserver()
    }

    private fun initAdapters() {
        favoriteListAdapter = RecipesListAdapter(emptyList())
        binding.rvFavorites.adapter = favoriteListAdapter

        favoriteListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun setupObserver() {

        favoritesViewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            favoriteListAdapter.updateRecipes(state.favoritesList)

            if (state.favoritesList.isEmpty()) {
                binding.rvFavorites.visibility = View.GONE
                binding.tvTitleFavoritesEmpty.visibility = View.VISIBLE
            } else {
                binding.rvFavorites.visibility = View.VISIBLE
                binding.tvTitleFavoritesEmpty.visibility = View.GONE
            }

            state.errorResId?.let { message ->
                Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show()
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
