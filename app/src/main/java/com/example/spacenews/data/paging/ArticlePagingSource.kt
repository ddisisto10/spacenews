package com.example.spacenews.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.spacenews.data.PagingError
import com.example.spacenews.data.model.Article
import com.example.spacenews.data.network.ApiClient
import retrofit2.HttpException
import java.io.IOException

class ArticlePagingSource(
    private val apiClient: ApiClient,
    private val searchQuery: String
) : PagingSource<Int, Article>() {

//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
//        val page = params.key ?: 1
//
//        return try {
//            val response = apiClient.getArticlesPaged(
//                search = searchQuery,
//                limit = params.loadSize,
//                offset = (page - 1) * params.loadSize
//            )
//
//            LoadResult.Page(
//                data = response.results,
//                prevKey = if (page == 1) null else page - 1,
//                nextKey = if (response.next == null) null else page + 1
//            )
//        } catch (e: IOException) {
//            LoadResult.Error(e)
//        } catch (e: HttpException) {
//            LoadResult.Error(e)
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
    val page = params.key ?: 1

    return try {
        val response = apiClient.getArticlesPaged(
                search = searchQuery,
                limit = params.loadSize,
                offset = (page - 1) * params.loadSize
            )

        LoadResult.Page(
            data = response.results,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (response.next == null) null else page + 1
        )
    } catch (e: IOException) {
        LoadResult.Error(PagingError.Network())
    } catch (e: HttpException) {
        LoadResult.Error(PagingError.Http(e.code(), "Error del servidor."))
    } catch (e: Exception) {
        LoadResult.Error(PagingError.Unknown())
    }
}

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
