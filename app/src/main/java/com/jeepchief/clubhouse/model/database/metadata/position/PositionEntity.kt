package com.jeepchief.clubhouse.model.database.metadata.position

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PositionEntity(
    @PrimaryKey
    var spposition: Int,

    @ColumnInfo(name = "desc")
    var desc: String
)
