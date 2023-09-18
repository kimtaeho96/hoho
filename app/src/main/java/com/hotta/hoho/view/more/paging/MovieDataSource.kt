package com.hotta.hoho.view.more.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hotta.hoho.datamodel.PopularMovieResult
import com.hotta.hoho.network.model.PopularMovieResponse
import com.hotta.hoho.repository.NetworkRepository
import retrofit2.HttpException
import java.lang.Exception


class MovieDataSource(val repository: NetworkRepository, val type: String) :
    PagingSource<Int, PopularMovieResult>() {
    lateinit var response: PopularMovieResponse
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularMovieResult> {
        return try {
            Log.d("MovieDataSource1", type)
            val currentPage = params.key ?: 1
            if (type == "now") {
                response = repository.getCurrentMovie(currentPage)
            } else if (type == "pop") {
                response = repository.getPopularMovie(currentPage)
            } else if (type == "top") {
                response = repository.getTopMovie(currentPage)
            } else {
                response = repository.getUpMovie(currentPage)
            }

            val data = response.movies
            val responseData = mutableListOf<PopularMovieResult>()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)

            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (httpE: HttpException) {
            LoadResult.Error(httpE)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, PopularMovieResult>): Int? {
        return null
    }
}


