package com.carlospinan.androidshushme.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(
    tableName = "place"
)
data class ShushPlace(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NotNull val placeId: String,
    @NotNull val latitude: Double,
    @NotNull val longitude: Double,
    @NotNull val name: String,
    @NotNull val address: String
)