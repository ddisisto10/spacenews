package com.example.spacenews.data.network

import com.example.space.data.model.ArticleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {
    @GET("articles")
    suspend fun getArticlesPaged(
        @Query("search") search: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ArticleResponse
}