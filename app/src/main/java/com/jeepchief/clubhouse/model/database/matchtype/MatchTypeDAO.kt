package com.jeepchief.clubhouse.model.database.matchtype

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MatchTypeDAO {
    @Query("SELECT * FROM MatchTypeEntity WHERE matchtype = :id")
    fun selectMatchType(id: Int) : MatchTypeEntity
    @Insert
    fun insertMatchType(entity: MatchTypeEntity)
}