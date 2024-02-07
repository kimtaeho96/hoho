package com.hotta.hoho.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hotta.hoho.db.dao.SearchDao
import com.hotta.hoho.db.entity.SearchListEntity
import java.time.Instant



@Database(entities = [SearchListEntity::class], version = 1)
abstract class SearchListDataBase : RoomDatabase() {
    abstract fun searchDao(): SearchDao

    companion object {
        @Volatile
        private var INSTANCE: SearchListDataBase? = null

        fun getDatabase(context: Context): SearchListDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SearchListDataBase::class.java,
                    "search_list_table"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}