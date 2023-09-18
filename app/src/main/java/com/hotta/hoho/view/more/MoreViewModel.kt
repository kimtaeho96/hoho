package com.hotta.hoho.view.more

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.hotta.hoho.repository.NetworkRepository
import com.hotta.hoho.view.more.paging.MovieDataSource


class MoreViewModel : ViewModel() {
    private val repository = NetworkRepository()

    val loading = MutableLiveData<Boolean>()
    var type: String = ""

    fun type(type: String) {
        this.type = type
    }

    val nowMovieList = Pager(PagingConfig(1)) {
        MovieDataSource(repository,type)
    }.flow.cachedIn(viewModelScope)

    val popMovieList = Pager(PagingConfig(1)) {
        MovieDataSource(repository, type)
    }.flow.cachedIn(viewModelScope)

    val topMovieList = Pager(PagingConfig(1)) {
        MovieDataSource(repository, type)
    }.flow.cachedIn(viewModelScope)

    val upMovieList = Pager(PagingConfig(1)) {
        MovieDataSource(repository, type)
    }.flow.cachedIn(viewModelScope)

}

