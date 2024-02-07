package com.hotta.hoho.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hotta.hoho.db.entity.SearchListEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface SearchDao {

    @Query("SELECT * FROM search_list_table ")
    fun getAllSearchData(): Flow<List<SearchListEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun searchDataInsert(searchListEntity: SearchListEntity)

    @Update
    fun update(searchListEntity: SearchListEntity)

    @Query("DELETE FROM search_list_table")
    fun delete()

   /* @Query("SELECT * FROM search_list_table WHERE selected =:selected")
    fun getSearchData(selected: Boolean = true) :List<SearchListEntity>*/

}