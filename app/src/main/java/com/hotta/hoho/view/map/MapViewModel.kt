package com.hotta.hoho.view.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotta.hoho.network.api.Api
import com.hotta.hoho.repository.NetworkRepository
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private val networkRepository = NetworkRepository()
    lateinit var kakaoSearchResult: ArrayList<Document>
    private val _kakaoMapResult = MutableLiveData<List<Document>>()
    val kakaoMapResult: LiveData<List<Document>>
        get() = _kakaoMapResult

    fun getKakaoMapSearch(query: String, x: Double, y: Double, radius: Int) =
        viewModelScope.launch {
            try {
                kakaoSearchResult = ArrayList()
                val result = networkRepository.getKakaoMapSearch(
                    "KakaoAK a3938b7a7b312662ff65b814874f0079",
                    query,
                    x.toString(),
                    y.toString(),
                    radius
                )

                for (item in result.documents) {
                    kakaoSearchResult.add(item)
                }
                Log.d("zzzzz", result.toString())
            } catch (e: Exception) {
                Log.d("zzzzz", e.toString())
            }
            _kakaoMapResult.value = kakaoSearchResult


        }
}