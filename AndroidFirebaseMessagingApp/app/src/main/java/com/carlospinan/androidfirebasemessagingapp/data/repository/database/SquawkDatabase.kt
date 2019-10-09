package com.carlospinan.androidfirebasemessagingapp.data.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Squawk::class],
    version = 1,
    exportSchema = false
)
abstract class SquawkDatabase : RoomDatabase() {

    abstract fun squawkDao(): SquawkDao

    companion object {

        @Volatile
        private var INSTANCE: SquawkDatabase? = null

        fun getInstance(context: Context): SquawkDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            SquawkDatabase::class.java,
                            "$DATABASE_NAME"
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}