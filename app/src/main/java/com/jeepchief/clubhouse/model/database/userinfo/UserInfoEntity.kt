package com.jeepchief.clubhouse.model.database.userinfo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserInfoEntity(
    @PrimaryKey
    var nickname: String,
    @ColumnInfo
    var uid: String,
    @ColumnInfo
    var level: Int
)