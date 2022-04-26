package com.jeepchief.clubhouse.model.database.metadata.position

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PositionDAO {
    @Query("SELECT * FROM PositionEntity WHERE spposition = :spposition")
    fun selectPosition(spposition: Int) : PositionEntity

    @Insert
    fun insertPosition(entity: PositionEntity)
}