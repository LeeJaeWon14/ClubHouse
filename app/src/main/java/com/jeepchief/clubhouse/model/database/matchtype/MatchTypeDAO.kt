package com.jeepchief.clubhouse.model.database.matchtype

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MatchTypeDAO {
    @Query("SELECT * FROM MatchTypeEntity WHERE 'desc' = :desc")
    fun selectMatchType(desc: String) : List<MatchTypeEntity>
    @Insert
    fun insertMatchType(entity: MatchTypeEntity)
}