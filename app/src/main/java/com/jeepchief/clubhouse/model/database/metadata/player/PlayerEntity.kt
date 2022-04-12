package com.jeepchief.clubhouse.model.database.metadata.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayerEntity(
    @PrimaryKey
    var spid: String,
    @ColumnInfo
    var name: String
)
