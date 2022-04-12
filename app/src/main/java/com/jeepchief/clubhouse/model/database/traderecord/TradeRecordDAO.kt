package com.jeepchief.clubhouse.model.database.traderecord

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TradeRecordDAO {
    @Query("SELECT * FROM TradeRecordEntity t WHERE t.uid = (SELECT u.uid FROM UserInfoEntity u WHERE u.nickname = :nickname)")
    fun selectTradeRecord(nickname: String) : List<TradeRecordEntity>

    @Insert
    fun insertTradeRecord(entity: TradeRecordEntity)
}