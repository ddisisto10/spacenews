package com.example.space.ui.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.spacenews.domain.GetArticlesUseCase
import com.example.spacenews.data.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.spacenews.ui.Base.BaseViewModel
import com.example.spacenews.ui.view.LoadingState


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase
): BaseViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _articlesPagingFlow = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val articlesPagingFlow: StateFlow<PagingData<Article>> = _articlesPagingFlow

    init {
        getArticlesPaged("")
    }

    fun getArticlesPaged(searchText: String) {
        _searchQuery.value = searchText
        viewModelScope.launch {
            getArticlesUseCase.getArticlesPaged(searchText)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _articlesPagingFlow.value = it
                }
        }
    }
}
