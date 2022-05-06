package com.jeepchief.clubhouse.viewmodel

import androidx.lifecycle.ViewModel
import com.jeepchief.clubhouse.model.database.userinfo.UserInfoEntity
import com.jeepchief.clubhouse.model.rest.dto.MatchBean
import com.jeepchief.clubhouse.model.rest.dto.TradeRecordDTO

class FifaViewModel : ViewModel() {
    var userId = ""

    val matchRecordMap: HashMap<Int, List<MatchBean>> = hashMapOf()
    var buyTradeRecordList: List<TradeRecordDTO> = listOf()
    var sellTradeRecordList: List<TradeRecordDTO> = listOf()
}