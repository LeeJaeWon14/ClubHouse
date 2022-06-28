package com.jeepchief.clubhouse.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.clubhouse.model.database.MyDatabase
import com.jeepchief.clubhouse.model.database.userinfo.UserInfoDAO
import com.jeepchief.clubhouse.model.database.userinfo.UserInfoEntity
import com.jeepchief.clubhouse.model.rest.dto.MatchBean
import com.jeepchief.clubhouse.model.rest.dto.TradeRecordDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FifaViewModel : ViewModel() {
    var userId = ""

    val matchRecordMap: HashMap<Int, List<MatchBean>> = hashMapOf()
    var buyTradeRecordList: List<TradeRecordDTO> = listOf()
    var sellTradeRecordList: List<TradeRecordDTO> = listOf()

    fun getUserInfo(context: Context) : UserInfoDAO {
        lateinit var dao: UserInfoDAO
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao = MyDatabase.getInstance(context).getUserInfoDAO()
            }
        }
        return dao
    }
}