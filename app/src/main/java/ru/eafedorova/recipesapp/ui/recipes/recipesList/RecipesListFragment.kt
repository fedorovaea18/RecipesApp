package ru.eafedorova.recipesapp.ui.recipes.recipesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.RecipeApplication
import ru.eafedorova.recipesapp.databinding.FragmentListRecipesBinding
import ru.eafedorova.recipesapp.model.Category

class RecipesListFragment : Fragment() {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for RecipesListFragment must not be null")

    private lateinit var recipesListViewModel: RecipesListViewModel

    private lateinit var recipeListAdapter: RecipesListAdapter

    private val args: RecipesListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireContext().applicationContext as RecipeApplication).appContainer
        recipesListViewModel = RecipesListViewModel(appContainer.repository)
    }

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
        val category = args.category

        initUI(category)
    }

    private fun initUI(category: Category) {
        recipesListViewModel.loadRecipeList(category)
        initAdapters()
        setupObserver()
    }

    private fun initAdapters() {
        recipeListAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipes.adapter = recipeListAdapter


        recipeListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun setupObserver() {

        recipesListViewModel.recipeListState.observe(viewLifecycleOwner) { state ->
            binding.tvTitleCategoryRecipe.text = state.categoryName
            loadImage(state)

            recipeListAdapter.updateRecipes(state.recipesList)

            state.errorResId?.let { message ->
                Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun loadImage(state: RecipesListViewModel.RecipeListState) {
        Glide
            .with(this)
            .load(state.categoryImageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.ivImageCategoryRecipe)

    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipeId
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}
