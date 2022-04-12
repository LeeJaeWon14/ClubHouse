package com.jeepchief.clubhouse.model.database.metadata.division

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DivisionDAO {
    @Query("SELECT * FROM DivisionEntity WHERE divisionId = :divisionId")
    fun getDivisionName(divisionId: String) : List<DivisionEntity>

    @Query("DELETE FROM DivisionEntity")
    fun deleteDivisionName()
}