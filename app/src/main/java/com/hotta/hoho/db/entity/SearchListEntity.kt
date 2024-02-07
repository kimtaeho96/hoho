package com.hotta.hoho.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_list_table")
data class SearchListEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val searchName: String,
    val data: String

)