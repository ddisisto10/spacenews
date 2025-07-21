package com.example.spacenews.domain

import androidx.paging.PagingData
import com.example.spacenews.data.Repository
import com.example.spacenews.data.model.Article
import com.example.spacenews.data.model.Author
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class GetArticlesUseCaseTest {

    private lateinit var useCase: GetArticlesUseCase
    private val repository: Repository = mock()

    @Before
    fun setup() {
        useCase = GetArticlesUseCase(repository)
    }

    @Test
    fun `should return PagingData when getArticlesPaged is called`() = runTest {
        // Arrange
        val article = Article(
            id = 1,
            title = "Título de prueba",
            authors = listOf(Author("Autor 1")),
            url = "https://ejemplo.com",
            image_url = "https://imagen.jpg",
            news_site = "NewsSite",
            summary = "Resumen",
            published_at = "2024-01-01",
            updated_at = "2024-01-02",
            featured = true
        )
        val pagingData = PagingData.from(listOf(article))
        val flow = flowOf(pagingData)

        whenever(repository.getArticlesPaged("nasa"))
            .thenReturn(flow)

        // Act
        val result = useCase.getArticlesPaged("nasa")

        // Assert
        assertNotNull(result)
        verify(repository).getArticlesPaged("nasa")
    }

    @Test
    fun `should call repository getArticlesPaged with correct parameters`() = runTest {
        // Arrange
        val emptyFlow: Flow<PagingData<Article>> = flowOf(PagingData.empty())
        whenever(repository.getArticlesPaged(any())).thenReturn(emptyFlow)
        
        // Act
        useCase.getArticlesPaged("spacex")
        
        // Assert
        verify(repository).getArticlesPaged("spacex")
    }
}
