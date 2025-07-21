package com.example.space.ui.Home

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spacenews.databinding.FragmentHomeBinding
import com.example.spacenews.data.model.Article
import com.example.spacenews.ui.Base.BaseFragment

import com.example.space.ui.Home.Adapter.ArticlesPagingAdapter
import com.example.spacenews.ui.view.LoadingState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment:BaseFragment<HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagingAdapter: ArticlesPagingAdapter
    private var recyclerViewState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagingAdapter = ArticlesPagingAdapter { article -> goToDetail(article) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreRecyclerViewState(savedInstanceState)
        initRecyclerView()
        initSearch()
        observeViewModel()
    }

    private fun initRecyclerView() = binding.rvArticles.apply {
        if (layoutManager == null) {
            layoutManager = LinearLayoutManager(requireContext())
        }
        adapter = this@HomeFragment.pagingAdapter
        
        // Configurar estados de carga
        pagingAdapter.addLoadStateListener { loadState ->
            val isLoading = loadState.refresh is LoadState.Loading ||
                    loadState.append is LoadState.Loading
            
            // Mostrar/ocultar un indicador de progreso
            if (isLoading) {
                LoadingState.show("Cargando...")
            } else {
                LoadingState.hide()
            }
        }
    }

    private fun initSearch() = binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            saveRecyclerViewState()
            viewModel.getArticlesPaged(query.orEmpty())
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean = false
    })

    private fun observeViewModel() {
        // Observa el flujo de datos paginados
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.articlesPagingFlow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)

                recyclerViewState?.let {
                    binding.rvArticles.layoutManager?.onRestoreInstanceState(it)
                    recyclerViewState = null
                }
            }
        }
        lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                val refreshState = loadStates.refresh
                if (refreshState is LoadState.Error) {
                    Log.e("HomeFragment", "Error : ${refreshState.error}")
                    handlePagingError(refreshState.error)
                }
            }
        }
    }

    private fun goToDetail(article: Article) {
        saveRecyclerViewState()
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment2(article)
        findNavController().navigate(action)
    }

    private fun saveRecyclerViewState() {
        recyclerViewState = binding.rvArticles.layoutManager?.onSaveInstanceState()
    }

    private fun restoreRecyclerViewState(savedInstanceState: Bundle?) {
        savedInstanceState?.getParcelable<Parcelable>(RECYCLER_VIEW_STATE_KEY)?.let {
            recyclerViewState = it
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.rvArticles?.layoutManager?.onSaveInstanceState()?.let {
            outState.putParcelable(RECYCLER_VIEW_STATE_KEY, it)
        }
    }

    override fun onPause() {
        super.onPause()
        saveRecyclerViewState()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val RECYCLER_VIEW_STATE_KEY = "recycler_view_state"
    }
}

