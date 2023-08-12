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
    @Insert(entity = ApiResult::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(repos: List<ApiResult>)

    @Query("SELECT * FROM resApi WHERE sectionId LIKE :section OR sectionName LIKE :section")
    fun getData(section : String) : PagingSource<Int, ApiResult>


    @Query("SELECT * FROM resApi")
    fun getAllData() : PagingSource<Int,ApiResult>

    @Query("DELETE FROM resApi WHERE sectionId LIKE :section OR sectionName LIKE :section")
    suspend fun deleteData(section : String)

    @Delete
    suspend fun deleteAllData(list : List<ApiResult> = emptyList())
}