package com.carlospinan.androidshushme.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carlospinan.androidshushme.data.entities.ShushPlace

@Database(
    exportSchema = false,
    version = 1,
    entities = [ShushPlace::class]
)
abstract class ShushDatabase : RoomDatabase() {

    abstract fun shushDao(): ShushDao

    companion object {

        @Volatile
        private var INSTANCE: ShushDatabase? = null

        fun getInstance(context: Context): ShushDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room
                        .databaseBuilder(
                            context,
                            ShushDatabase::class.java,
                            "shush.db"
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