package com.example.myproj.roomDataBase

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myproj.model.ApiResult

@Dao
interface NewsDao {
    @Insert(entity = ApiResult::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<ApiResult>)

    @Query("SELECT * FROM resApi WHERE sectionId LIKE :section OR sectionName LIKE :section")
    fun getData(section : String) : PagingSource<Int, ApiResult>


    @Query("SELECT * FROM resApi")
    fun getAllData() : PagingSource<Int,ApiResult>

    @Query("DELETE FROM resApi WHERE sectionId LIKE :section OR sectionName LIKE :section")
    suspend fun deleteData(section : String)

    @Query("DELETE FROM resApi WHERE sectionId NOT LIKE 'world' AND sectionId NOT LIKE 'sport'" +
            "AND sectionId NOT LIKE 'science' AND sectionId NOT LIKE 'environment'")
    suspend fun deleteAllData()
}