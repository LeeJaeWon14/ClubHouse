package com.jeepchief.clubhouse.model.database.metadata.matchtype

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MatchTypeDAO {
    @Query("SELECT * FROM MatchTypeEntity WHERE matchtype = :id")
    fun selectMatchType(id: Int) : MatchTypeEntity

    @Query("SELECT matchtype FROM MatchTypeEntity WHERE `desc` = :desc")
    fun selectMatchTypeId(desc: String) : Int

    @Query("SELECT `desc` FROM MatchTypeEntity")
    fun selectAllMatchType() : List<String>

    @Insert
    fun insertMatchType(entity: MatchTypeEntity)
}