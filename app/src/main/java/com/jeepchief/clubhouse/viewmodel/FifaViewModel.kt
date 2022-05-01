package com.jeepchief.clubhouse.viewmodel

import androidx.lifecycle.ViewModel
import com.jeepchief.clubhouse.model.database.userinfo.UserInfoEntity
import com.jeepchief.clubhouse.model.rest.dto.MatchBean

class FifaViewModel : ViewModel() {
    val test = 123
    var userInfo: UserInfoEntity? = null

    val matchRecordMap: HashMap<Int, List<MatchBean>> = hashMapOf()
}