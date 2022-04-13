package com.jeepchief.clubhouse.model.database.metadata.division

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DivisionDAO {
    @Query("SELECT * FROM DivisionEntity WHERE divisionId = :divisionId")
    fun selectDivisionName(divisionId: Int) : DivisionEntity

    @Insert
    fun insertDivision(entity: DivisionEntity)

    @Query("DELETE FROM DivisionEntity")
    fun deleteDivisionName()
}