package com.jeepchief.clubhouse.model.database.traderecord

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TradeRecordEntity(
    @ColumnInfo
    var tradeDate: String,

    @ColumnInfo
    var saleSn: String,

    @PrimaryKey
    var spid: Int,

    @ColumnInfo
    var grade: Int,

    @ColumnInfo
    var value: Int
)
