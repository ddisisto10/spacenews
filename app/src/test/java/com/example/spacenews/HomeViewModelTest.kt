package com.example.spacenews // Asegúrate que el package sea el correcto
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.example.spacenews.data.NetworkResult
import com.example.spacenews.data.Repository
import com.example.spacenews.data.model.Article
import com.example.spacenews.data.model.Author
import com.example.spacenews.domain.GetArticlesUseCase
import com.example.spacenews.ui.view.LoadingState
import com.example.space.ui.Home.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getArticlesUseCase: GetArticlesUseCase

    // El objeto a probar
    private lateinit var viewModel: HomeViewModel

    // Dispatcher para controlar las corrutinas en los tests
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)

        // Crea el mock
        getArticlesUseCase = mockk(relaxed = true)

        //ViewModel con el use case mockeado
        viewModel = HomeViewModel(getArticlesUseCase)

        mockkObject(LoadingState)
    }


    @After
    fun tearDown() {
        // Limpia el dispatcher principal después de cada test
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando getArticlesPaged es llamado, se obtiene el flujo de PagingData`() = runTest {
       //Crea una lista de artículos de prueba
        val mockArticle = Article(
            id = 1,
            title = "Explorando Kotlin Multiplatform",
            authors = listOf(Author("JetBrains"), Author("Android Team")),
            url = "https://kotlinlang.org/docs/multiplatform.html",
            image_url = "https://example.com/image1.jpg",
            news_site = "Kotlin News",
            summary = "Una guía para comenzar con Kotlin Multiplatform.",
            published_at = "2024-05-10T12:00:00Z",
            updated_at = "2024-05-11T08:30:00Z",
            featured = true
        )
        
        //Crea un PagingData con los artículos de prueba
        val pagingData = PagingData.from(listOf(mockArticle))
        val flow: Flow<PagingData<Article>> = flowOf(pagingData)
        
        // Configura el mock para que devuelva el flujo de PagingData
        every { getArticlesUseCase.getArticlesPaged(any()) } returns flow
        

        // Llama a la función que queremos probar
        viewModel.getArticlesPaged("nasa")
        

        // Verifica que el caso de uso fue llamado exactamente una vez con el parámetro correcto
        verify(exactly = 1) { getArticlesUseCase.getArticlesPaged("nasa") }
    }
    
    @Test
    fun `cuando se inicializa el ViewModel, se llama a getArticlesPaged con texto vacío`() = runTest {

        val emptyFlow: Flow<PagingData<Article>> = flowOf(PagingData.empty())
        every { getArticlesUseCase.getArticlesPaged("") } returns emptyFlow

        val newViewModel = HomeViewModel(getArticlesUseCase)
        
        // Assert
        verify { getArticlesUseCase.getArticlesPaged("") }
    }
}