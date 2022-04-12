package com.jeepchief.clubhouse.model.database.matchrecord

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MatchRecordEntity(
    @PrimaryKey
    var uid: String
)
