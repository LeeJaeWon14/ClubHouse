package com.jeepchief.clubhouse.model.database.metadata.player

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerDAO {
    @Query("SELECT * FROM PlayerEntity WHERE spid = :spid")
    fun selectPlayer(spid: String) : List<PlayerEntity>

    @Insert
    fun insertPlayer(entity: PlayerEntity)

    @Query("DELETE FROM PlayerEntity")
    fun deletePlayer()
}