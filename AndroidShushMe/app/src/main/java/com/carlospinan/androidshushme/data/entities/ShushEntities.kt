package com.carlospinan.androidshushme.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(
    tableName = "place"
)
data class Place(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NotNull val placeId: Long
)