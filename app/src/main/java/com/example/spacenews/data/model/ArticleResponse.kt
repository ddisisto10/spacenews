package com.example.space.data.model

import com.example.spacenews.data.model.Article

data class ArticleResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Article>
)