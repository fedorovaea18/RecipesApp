package ru.eafedorova.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.RecipeApplication
import ru.eafedorova.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for CategoriesListFragment must not be null")

    private lateinit var categoriesListViewModel: CategoriesListViewModel

    private lateinit var categoriesListAdapter: CategoriesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireContext().applicationContext as RecipeApplication).appContainer
        categoriesListViewModel = appContainer.categoriesListViewModel.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        categoriesListViewModel.loadCategories()
        initAdapters()
        setupObserver()
    }

    private fun initAdapters() {
        categoriesListAdapter = CategoriesListAdapter(emptyList())
        binding.rvCategories.adapter = categoriesListAdapter

        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    private fun setupObserver() {

        categoriesListViewModel.categoriesListState.observe(viewLifecycleOwner) { state ->
            categoriesListAdapter.updateCategories(state.categoriesList)

            state.errorResId?.let { message ->
                Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category =
            categoriesListViewModel.categoriesListState.value?.categoriesList?.find { it.id == categoryId }
                ?: throw IllegalArgumentException("Category not found")

        findNavController().navigate(
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                category
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
