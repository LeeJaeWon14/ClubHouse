package com.jeepchief.clubhouse.model.database.metadata.division

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DivisionEntity(
    @PrimaryKey(autoGenerate = false)
    var divisionId: String,

    @ColumnInfo
    var divisionName: String
)
