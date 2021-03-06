package com.jeepchief.clubhouse.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeepchief.clubhouse.model.database.matchrecord.MatchRecordDAO
import com.jeepchief.clubhouse.model.database.matchrecord.MatchRecordEntity
import com.jeepchief.clubhouse.model.database.metadata.division.DivisionDAO
import com.jeepchief.clubhouse.model.database.metadata.division.DivisionEntity
import com.jeepchief.clubhouse.model.database.metadata.matchtype.MatchTypeDAO
import com.jeepchief.clubhouse.model.database.metadata.matchtype.MatchTypeEntity
import com.jeepchief.clubhouse.model.database.metadata.player.PlayerDAO
import com.jeepchief.clubhouse.model.database.metadata.player.PlayerEntity
import com.jeepchief.clubhouse.model.database.metadata.position.PositionDAO
import com.jeepchief.clubhouse.model.database.metadata.position.PositionEntity
import com.jeepchief.clubhouse.model.database.traderecord.TradeRecordDAO
import com.jeepchief.clubhouse.model.database.traderecord.TradeRecordEntity
import com.jeepchief.clubhouse.model.database.userinfo.UserInfoDAO
import com.jeepchief.clubhouse.model.database.userinfo.UserInfoEntity

@Database(
    entities = [
        UserInfoEntity::class,
        TradeRecordEntity::class,
        MatchRecordEntity::class,
        PlayerEntity::class,
        MatchTypeEntity::class,
        DivisionEntity::class,
        PositionEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getUserInfoDAO() : UserInfoDAO
    abstract fun getTradeRecordDAO() : TradeRecordDAO
    abstract fun getMatchRecordDAO() : MatchRecordDAO
    abstract fun getPlayerDAO() : PlayerDAO
    abstract fun getMatchTypeDAO() : MatchTypeDAO
    abstract fun getDivisionDAO() : DivisionDAO
    abstract fun getPositionDAO() : PositionDAO

    companion object {
        private var instance: MyDatabase? = null
        @Synchronized
        fun getInstance(context: Context) : MyDatabase {
            instance?.let {
                return it
            } ?: run {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "clubHouse.db"
                ).fallbackToDestructiveMigration().build()
                return instance!!
            }
        }
    }
}