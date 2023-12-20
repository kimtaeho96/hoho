package com.hotta.hoho.view.credit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotta.hoho.datamodel.CreditsMovieResult
import com.hotta.hoho.datamodel.MovieDto
import com.hotta.hoho.datamodel.PeopleMovieResult
import com.hotta.hoho.network.model.PeopleDetailResponse
import com.hotta.hoho.network.model.PeopleMovieResponse
import com.hotta.hoho.repository.NetworkRepository
import com.hotta.hoho.utils.MLOG
import com.hotta.hoho.view.join.UserModel
import kotlinx.coroutines.launch

class CreditViewModel : ViewModel() {
    private val networkRepository = NetworkRepository()
    lateinit var peopleMovieList: ArrayList<PeopleMovieResult>


    private var _peopleDetail = MutableLiveData<PeopleDetailResponse>()
    val peopleDetail: LiveData<PeopleDetailResponse>
        get() = _peopleDetail

    private var _peopleMovie = MutableLiveData<List<PeopleMovieResult>>()
    val peopleMovie: LiveData<List<PeopleMovieResult>>
        get() = _peopleMovie

    fun getPeopleDetail(id: Int) = viewModelScope.launch {

        try {
            val result = networkRepository.getPeopleDetail(id)

            _peopleDetail.value = result

            Log.d("(PeopleDetail)", result.toString())


        } catch (e: Exception) {
            Log.d("(PeopleDetail)", e.toString())

        }
    }

    fun getPeopleMovie(id: Int) = viewModelScope.launch {

        try {
            val result = networkRepository.getPeopleMovie(id)
            peopleMovieList = ArrayList()
            for (item in result.cast) {
                peopleMovieList.add(item)
            }
            _peopleMovie.value = peopleMovieList

            Log.d("(PeopleMovie)", result.toString())


        } catch (e: Exception) {
            Log.d("(PeopleMovie)", e.toString())

        }
    }

}