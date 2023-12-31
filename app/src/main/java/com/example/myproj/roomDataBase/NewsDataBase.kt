package com.example.myproj.roomDataBase



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myproj.model.ApiResult
import com.example.myproj.model.Converters

@Database(
    entities = [ApiResult::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NewsDataBase:RoomDatabase(){
    abstract fun newsDao(): NewsDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    companion object {
        @Volatile
        private var INSTANCE: NewsDataBase? = null
        fun getInstance(context: Context): NewsDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }
        fun refreshInstance(context: Context):NewsDataBase {
            INSTANCE = null
            return buildDatabase(context).also { INSTANCE = it }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NewsDataBase::class.java, "News20.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }


}
