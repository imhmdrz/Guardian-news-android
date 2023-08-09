package com.example.myproj.roomDataBase

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myproj.model.ApiResult

@Dao
interface NewsDao {
    @Insert(entity = ApiResult::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: ApiResult)

    @Insert(entity = ApiResult::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<ApiResult>)

    @Query("SELECT * FROM resApi WHERE sectionName = :section OR sectionId = :section" +
            " ORDER BY webPublicationDate DESC")
    fun getData(section : String) : PagingSource<Int, ApiResult>

    @Query("SELECT * FROM resApi")
    fun getAllData() : PagingSource<Int,ApiResult>

    @Query("DELETE FROM resApi WHERE sectionName = :section")
    suspend fun deleteData(section : String)

    @Query("DELETE FROM resApi")
    suspend fun deleteAllData()
}