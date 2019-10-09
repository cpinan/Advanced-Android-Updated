package com.carlospinan.androidfirebasemessagingapp.data.repository.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "$TABLE_SQUAWK")
data class Squawk(
    @PrimaryKey(autoGenerate = true) @NonNull @ColumnInfo(name = "_id") val id: Int,
    @NonNull val author: String,
    @NonNull val authorKey: String,
    @NonNull val message: String,
    @NonNull val date: Int
)