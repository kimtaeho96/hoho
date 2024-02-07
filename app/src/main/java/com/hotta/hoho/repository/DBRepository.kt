package com.hotta.hoho.repository

import com.google.firebase.database.core.Context
import com.hotta.hoho.App
import com.hotta.hoho.db.SearchListDataBase
import com.hotta.hoho.db.entity.SearchListEntity

class DBRepository {

    val context = App.context()
    val db = SearchListDataBase.getDatabase(context)

    fun getAllSearchData() = db.searchDao().getAllSearchData()

    fun insertSearchData(searchDataInsert: SearchListEntity) =
        db.searchDao().searchDataInsert(searchDataInsert)

    fun updateSearchData(searchDataInsert: SearchListEntity) =
        db.searchDao().update(searchDataInsert)

    fun deleteSearchData() = db.searchDao().allDeleteData()

    fun selectDeleteData(searchData: String) = db.searchDao().selectDeleteData(searchData)


}