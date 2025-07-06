package ru.eafedorova.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.eafedorova.recipesapp.Constants.ARG_CATEGORY_ID
import ru.eafedorova.recipesapp.Constants.ARG_CATEGORY_IMAGE_URL
import ru.eafedorova.recipesapp.Constants.ARG_CATEGORY_NAME
import ru.eafedorova.recipesapp.R
import ru.eafedorova.recipesapp.data.STUB
import ru.eafedorova.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for CategoriesListFragment must not be null")

    private val viewModel: CategoriesListViewModel by viewModels()

    private lateinit var categoriesListAdapter: CategoriesListAdapter

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
        viewModel.loadCategories()
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

        viewModel.categoriesListState.observe(viewLifecycleOwner) { state ->
            categoriesListAdapter.updateCategories(state.categoriesList)
        }
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val categoryName = STUB.getCategoryById(categoryId)?.title
        val categoryImageUrl = STUB.getCategoryById(categoryId)?.imageUrl
        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl
        )
        findNavController().navigate(R.id.recipesListFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
