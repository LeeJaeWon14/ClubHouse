package com.jeepchief.clubhouse.model.rest

import com.jeepchief.clubhouse.model.rest.dto.MatchTypeDTO
import com.jeepchief.clubhouse.model.rest.dto.MaxDivisionDTO
import com.jeepchief.clubhouse.model.rest.dto.PlayerDTO
import com.jeepchief.clubhouse.model.rest.dto.UserInfoDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FifaService {
    @GET("users")
    fun getUserInfo(
        @Query("nickname") nickname: String
    ): Call<UserInfoDTO>

    @GET(NetworkConstants.PLAYER_DATA)
    fun getPlayerData() : Call<List<PlayerDTO>>

    @GET(NetworkConstants.MAX_DIVISION_URL)
    fun getMaxDivision(@Path("accessid") uid: String) : Call<List<MaxDivisionDTO>>

    @GET(NetworkConstants.MATCH_TYPE_URL)
    fun getMatchType() : Call<List<MatchTypeDTO>>
}