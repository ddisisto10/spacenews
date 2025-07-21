package com.example.spacenews.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.spacenews.data.model.Article
import com.example.spacenews.data.network.ApiClient
import com.example.spacenews.data.paging.ArticlePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiClient: ApiClient
) {

    fun getArticlesPaged(searchText: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            pagingSourceFactory = { ArticlePagingSource(apiClient, searchText) }
        ).flow
    }
}