package com.hotta.hoho.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hotta.hoho.datamodel.CreditsMovieResult
import com.hotta.hoho.datamodel.MovieDto
import com.hotta.hoho.datamodel.PopularMovieResult
import com.hotta.hoho.datamodel.PopularTvResult
import com.hotta.hoho.datamodel.ViewMovieResult
import com.hotta.hoho.datamodel.WeatherResult
import com.hotta.hoho.network.model.DetailMovieResponse
import com.hotta.hoho.repository.NetworkRepository
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.view.detail.ReviewModel
import com.hotta.hoho.view.join.UserModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel : ViewModel() {


    private val networkRepository = NetworkRepository()

    lateinit var movieDto: ArrayList<MovieDto>
    lateinit var popularMovieList: ArrayList<PopularMovieResult>
    lateinit var detailMovieList: ArrayList<DetailMovieResponse>
    lateinit var videoMovieList: ArrayList<ViewMovieResult>
    lateinit var creditsMovieList: ArrayList<CreditsMovieResult>
    lateinit var popularTvList: ArrayList<PopularTvResult>
    lateinit var genreMovieList: ArrayList<PopularMovieResult>
    lateinit var currentMovieList: ArrayList<PopularMovieResult>
    lateinit var topMovieList: ArrayList<PopularMovieResult>
    lateinit var upMovieList: ArrayList<PopularMovieResult>
    lateinit var weatherList: ArrayList<WeatherResult.Item>


    private val _dayMovieResult = MutableLiveData<List<MovieDto>>()
    val dayMovieResult: LiveData<List<MovieDto>>
        get() = _dayMovieResult

    private val _popMovieResult = MutableLiveData<List<PopularMovieResult>>()
    val popMovieResult: LiveData<List<PopularMovieResult>>
        get() = _popMovieResult

    private val _curMovieResult = MutableLiveData<List<PopularMovieResult>>()
    val curMovieResult: LiveData<List<PopularMovieResult>>
        get() = _curMovieResult

    private val _topMovieResult = MutableLiveData<List<PopularMovieResult>>()
    val topMovieResult: LiveData<List<PopularMovieResult>>
        get() = _topMovieResult

    private val _upMovieResult = MutableLiveData<List<PopularMovieResult>>()
    val upMovieResult: LiveData<List<PopularMovieResult>>
        get() = _upMovieResult

    private val _detailMoviResult = MutableLiveData<List<DetailMovieResponse>>()
    val detailMovieResult: LiveData<List<DetailMovieResponse>>
        get() = _detailMoviResult

    private val _videoMovieResult = MutableLiveData<String>()
    val videoMovieResult: LiveData<String>
        get() = _videoMovieResult

    private val _creditsMovieResult = MutableLiveData<List<CreditsMovieResult>>()
    val creditMovieResult: LiveData<List<CreditsMovieResult>>
        get() = _creditsMovieResult

    private var _userData = MutableLiveData<UserModel>()
    val userData: LiveData<UserModel>
        get() = _userData

    private val _popTvResult = MutableLiveData<List<PopularTvResult>>()
    val popTvResult: LiveData<List<PopularTvResult>>
        get() = _popTvResult

    private val _weatherResult = MutableLiveData<List<WeatherResult.Item>>()
    val weatherResult: LiveData<List<WeatherResult.Item>>
        get() = _weatherResult


    private val _genereMovieResult = MutableLiveData<PopularMovieResult>()
    val genereMovieResult: LiveData<PopularMovieResult>
        get() = _genereMovieResult

    fun getDayMovie() = viewModelScope.launch {
        val result =
            networkRepository.getCurrentMovieList(
                "20230510",
                "c8903bb444c33549b65d68283c735827"
            )
        movieDto = ArrayList()
        try {
            for (movie in result.boxofficeResult?.dailyBoxOfficeList!!) {
                movieDto.add(movie)

            }
            Log.d("MainViewModel", movieDto.toString())
            _dayMovieResult.value = movieDto
        } catch (e: Exception) {
            Log.d("MainViewModel", e.toString())
        }
    }

    fun getPopularMovie() = viewModelScope.launch {
        val result = networkRepository.getPopularMovie(1)
        Log.d("MainViewModel(popular)", result.toString())
        popularMovieList = ArrayList()

        try {
            for (pop_movie in result.movies) {
                popularMovieList.add(pop_movie)
            }
            _popMovieResult.value = popularMovieList

            Log.d("MainViewModel(popular)", popularMovieList.toString())
        } catch (e: Exception) {
            Log.d("MainViewModel(popular)", e.toString())
        }
    }

    fun getCurrentMovie() = viewModelScope.launch {
        val result = networkRepository.getCurrentMovie(1)
        Log.d("MainViewModel(popular)", result.toString())
        currentMovieList = ArrayList()

        try {
            for (cur_movie in result.movies) {
                currentMovieList.add(cur_movie)
            }
            _curMovieResult.value = currentMovieList

            Log.d("MainViewModel(current)", currentMovieList.toString())
        } catch (e: Exception) {
            Log.d("MainViewModel(current)", e.toString())
        }
    }

    fun getTopMovie() = viewModelScope.launch {
        val result = networkRepository.getTopMovie(1)
        Log.d("MainViewModel(popular)", result.toString())
        topMovieList = ArrayList()

        try {
            for (top_movie in result.movies) {
                topMovieList.add(top_movie)
            }
            _topMovieResult.value = topMovieList

            Log.d("MainViewModel(top)", topMovieList.toString())
        } catch (e: Exception) {
            Log.d("MainViewModel(top)", e.toString())
        }
    }

    fun getUpMovie() = viewModelScope.launch {
        //        val result = networkRepository.getUpMovie("8f20c3de95e081c58a1a1ca38e4f7d73")

        val result = networkRepository.getUpMovie(1)
        Log.d("MainViewModel(Up)", result.toString())
        upMovieList = ArrayList()

        try {
            for (up_movie in result.movies) {
                upMovieList.add(up_movie)
            }
            _upMovieResult.value = upMovieList

            Log.d("MainViewModel(Up)", topMovieList.toString())
        } catch (e: Exception) {
            Log.d("MainViewModel(Up)", e.toString())
        }
    }

    fun getDetailMovie(id: Int) = viewModelScope.launch {
        try {
            val result = networkRepository.getDeltailMovie(id, "8f20c3de95e081c58a1a1ca38e4f7d73")
            Log.d("MainViewModel(Detail)", result.toString())


            _detailMoviResult.value = listOf(result)

        } catch (e: Exception) {
            Log.d("MainViewModel(Detail)", e.toString())
        }


    }

    fun getVideoMovie(id: Int) = viewModelScope.launch {
        try {
            val result = networkRepository.getViedoMovie(id)

            videoMovieList = ArrayList()
            for (item in result.result) {
                videoMovieList.add(item)
            }

            _videoMovieResult.value = videoMovieList[0].key

        } catch (e: Exception) {
            Log.d("MainViewModel(Vide)", e.toString())
        }


    }

    fun getCreditsMovie(id: Int) = viewModelScope.launch {

        try {
            val result = networkRepository.getCreditsMovie(id)
            creditsMovieList = ArrayList()
            for (item in result.cast) {
                if (item.known_for_department.contains("Acting") && item.profile_path != null) {
                    creditsMovieList.add(item)
                }
            }

            _creditsMovieResult.value = creditsMovieList

            Log.d("MainViewModel(Credits)", result.toString())

        } catch (e: Exception) {
            Log.d("MainViewModel(Credits)", e.toString())
        }
    }

    fun getUserInfo(uid: String) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    Log.d("MainViewModel3", uid.toString())
                    val data = dataSnapshot.getValue(UserModel::class.java)
                    Log.d("MainViewModel3", data.toString())
                    _userData.value = data!!
                } catch (e: java.lang.Exception) {
                    Log.d("MainViewModel3", e.toString())

                }


            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FireBaseRef.userInfo.child(uid).addValueEventListener(postListener)


    }

    fun getPopularTv() = viewModelScope.launch {
        val result = networkRepository.getPopularTv("8f20c3de95e081c58a1a1ca38e4f7d73")
        Log.d("MainViewModel(popular)1", result.toString())
        popularTvList = ArrayList()

        try {
            for (pop_Tv in result.results) {
                popularTvList.add(pop_Tv)
            }
            _popTvResult.value = popularTvList

            Log.d("MainViewModel(popular)2", popularTvList.toString())
        } catch (e: Exception) {
            Log.d("MainViewModel(popular)3", e.toString())
        }
    }

    fun getMoviesByGenre(genre: String) = viewModelScope.launch {
        val result =
            networkRepository.getMoviesByGenre("8f20c3de95e081c58a1a1ca38e4f7d73", genre)
        Log.d("MainViewModel(genre)1", result.toString())
        genreMovieList = ArrayList()

        try {
            for (pop_Tv in result.movies) {
                genreMovieList.add(pop_Tv)
            }

            val randomMovie = genreMovieList.random()
            Log.d("MainViewModel(random)1", randomMovie.toString())
            Log.d("MainViewModel(random)2", randomMovie.id.toString())
            Log.d("MainViewModel(random)3", randomMovie.poster_path.toString())

            _genereMovieResult.value = randomMovie

            Log.d("MainViewModel(genre)2", genreMovieList.toString())
        } catch (e: Exception) {
            Log.d("MainViewModel(genre)3", e.toString())
        }
    }


    fun getWeather() = viewModelScope.launch {

        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = Date()
        val today = dateFormat.format(currentDate).toInt()


        Log.d("weatherList", today.toString())
        val result = networkRepository.getWeather(
            "JSON", 14, 1,
            today, 1100, "37", "127"
        )

        weatherList = ArrayList()

        try {
            for (item in result.body()?.response!!.body.items.item) {
                Log.d("weatherList", "$item")
                weatherList.add(item)
            }
            _weatherResult.value = weatherList

        } catch (e: java.lang.Exception) {
            Log.d("weatherList", "$e")
        }

    }

}