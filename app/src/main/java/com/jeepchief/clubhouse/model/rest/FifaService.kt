package com.jeepchief.clubhouse.model.rest

import com.jeepchief.clubhouse.model.rest.dto.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FifaService {
    @GET("users")
    fun getUserInfo(
        @Query("nickname") nickname: String
    ): Call<UserInfoDTO>

    @GET("users")
    fun getUserInfoWithId(
        @Path("accessid") accessId: String
    ): Call<UserInfoDTO>

    @GET(NetworkConstants.PLAYER_DATA)
    fun getPlayerData() : Call<List<PlayerDTO>>

    @GET(NetworkConstants.MAX_DIVISION_URL)
    fun getMaxDivision(@Path("accessid") uid: String) : Call<List<MaxDivisionDTO>>

    @GET(NetworkConstants.MATCH_TYPE_URL)
    fun getMatchType() : Call<List<MatchTypeDTO>>

    @GET(NetworkConstants.DIVISION_URL)
    fun getDivisionData() : Call<List<DivisionDTO>>

    @GET(NetworkConstants.TRADE_RECORD_URL)
    fun getTradeRecord(
        @Path("accessid") uid: String,
        @Query("tradetype") tradeType: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ) : Call<List<TradeRecordDTO>>

    @GET(NetworkConstants.MATCH_DETAIL_URL)
    fun getMatchRecord(@Path("matchid") matchId: String) : Call<MatchBean>

    @GET(NetworkConstants.MATCH_RECORD_ID_URL)
    fun getMatchId(
        @Path("accessid") uid: String,
        @Query("matchtype") matchType: Int,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10
    ) : Call<List<String>>
}