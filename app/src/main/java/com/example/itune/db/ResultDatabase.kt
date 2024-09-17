package com.example.itune.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.itune.Results

@Database(
    entities = [Results::class],
    version = 1
)
abstract class ResultDatabase : RoomDatabase() {

    abstract fun getResultsDao() : ResultsDao

    companion object {
        @Volatile
        private var instance : ResultDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ResultDatabase::class.java,
            "result_db.db"
        ).build()

    }
}