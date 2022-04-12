package com.jeepchief.clubhouse.model.database.matchtype

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MatchTypeEntity(
    @PrimaryKey
    var matchtype: Int,
    @ColumnInfo
    var desc: String
)