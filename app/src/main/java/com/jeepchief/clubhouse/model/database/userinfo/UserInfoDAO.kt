package com.jeepchief.clubhouse.model.database.userinfo

import androidx.room.*

@Dao
interface UserInfoDAO {
    @Query("SELECT * FROM UserInfoEntity")
    fun selectUserInfo() : List<UserInfoEntity>

    @Insert
    fun insertUserInfo(entity: UserInfoEntity)

    @Delete
    fun deleteUserInfo(entity: UserInfoEntity)

    @Update
    fun updateUserInfo(entity: UserInfoEntity)

    @Query("SELECT * FROM UserInfoEntity WHERE nickname = :nickname")
    fun checkDistinctUserInfo(nickname: String) : List<UserInfoEntity>
}