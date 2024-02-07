package com.hotta.hoho.view.search

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hotta.hoho.db.entity.SearchListEntity
import com.hotta.hoho.network.model.MovieSearchResult
import com.hotta.hoho.repository.DBRepository
import com.hotta.hoho.repository.NetworkRepository
import com.hotta.hoho.utils.MyInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchViewModel : ViewModel() {
    private val networkRepository = NetworkRepository()
    private val dbRepository = DBRepository()

    lateinit var searchDataList: LiveData<List<SearchListEntity>>

    private var _movieSearch = MutableLiveData<List<MovieSearchResult>>()
    val movieSearch: LiveData<List<MovieSearchResult>>
        get() = _movieSearch

    lateinit var movieSearchList: ArrayList<MovieSearchResult>
    fun getSearchMovie(movieName: String) = viewModelScope.launch {
        Log.d("(getSearchMovie)", movieName)
        try {
            val result = networkRepository.getSearchMovie(movieName)

            movieSearchList = ArrayList()

            for (item in result.results) {
                movieSearchList.add(item)
            }
            _movieSearch.value = movieSearchList

            Log.d("(getSearchMovie)", result.toString())

        } catch (e: Exception) {
            Log.d("(getSearchMovie)", e.toString())

        }


    }

    fun getAllSearchData() = viewModelScope.launch {
        val searchList = dbRepository.getAllSearchData().asLiveData()
        searchDataList = searchList
    }

    fun getAllDeleteData() = viewModelScope.launch(Dispatchers.IO) {

        dbRepository.deleteSearchData()
    }

    //DB에 데이터저장
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveSearchData(searchData: String) = viewModelScope.launch(Dispatchers.IO) {
        dbRepository.getAllSearchData()
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val current = currentDate.format(formatter)

        val searchListEntity = SearchListEntity(
            0, searchData, current

        )
        searchListEntity.let {
            dbRepository.insertSearchData(it)
        }
    }

}
