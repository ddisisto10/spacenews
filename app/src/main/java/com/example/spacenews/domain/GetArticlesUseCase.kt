package com.example.spacenews.domain

import androidx.paging.PagingData
import com.example.spacenews.data.Repository
import com.example.spacenews.data.NetworkResult
import com.example.space.data.model.ArticleResponse
import com.example.spacenews.data.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetArticlesUseCase @Inject constructor(private val defaultRepository: Repository) {
    fun getArticlesPaged(searchText: String): Flow<PagingData<Article>> = defaultRepository.getArticlesPaged(searchText)
}