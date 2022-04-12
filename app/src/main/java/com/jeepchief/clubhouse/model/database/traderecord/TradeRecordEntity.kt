package com.jeepchief.clubhouse.model.database.traderecord

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TradeRecordEntity(
    @PrimaryKey
    var uid: String
)
