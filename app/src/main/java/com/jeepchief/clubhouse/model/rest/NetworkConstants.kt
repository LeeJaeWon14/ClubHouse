package com.jeepchief.clubhouse.model.rest

object NetworkConstants {
    // Authorization key
    const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiMjA0Njk2NDQ4NCIsImF1dGhfaWQiOiIyIiwidG9rZW5fdHlwZSI6IkFjY2Vzc1Rva2VuIiwic2VydmljZV9pZCI6IjQzMDAxMTQ4MSIsIlgtQXBwLVJhdGUtTGltaXQiOiI1MDA6MTAiLCJuYmYiOjE2NDg1NjQwODQsImV4cCI6MTY2NDExNjA4NCwiaWF0IjoxNjQ4NTY0MDg0fQ.uyNQO0Qj7N1O6eUhAc__Z7SkYGCScKwVI8Crby8T-rA"

    // URL
    const val BASE_URL = "https://api.nexon.co.kr/fifaonline4/v1.0/"
    const val PLAYER_DATA = "https://static.api.nexon.co.kr/fifaonline4/latest/spid.json"
    const val MAX_DIVISION_URL = "https://api.nexon.co.kr/fifaonline4/v1.0/users/{accessid}/maxdivision"
    const val MATCH_TYPE_URL = "https://static.api.nexon.co.kr/fifaonline4/latest/matchtype.json"
    const val DIVISION_URL = "https://static.api.nexon.co.kr/fifaonline4/latest/division.json"
    const val TRADE_RECORD_URL = "https://api.nexon.co.kr/fifaonline4/v1.0/users/{accessid}/markets?"
    const val PLAYER_ACTION_SHOT_URL = "https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p%s.png"
    const val PLAYER_IMAGE_URL = "https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/players/p%s.png"
    const val MATCH_DETAIL_URL = "https://api.nexon.co.kr/fifaonline4/v1.0/matches/{matchid}"
    const val MATCH_RECORD_ID_URL = "https://api.nexon.co.kr/fifaonline4/v1.0/users/{accessid}/matches?"
    const val POSITION_URL = "https://static.api.nexon.co.kr/fifaonline4/latest/spposition.json"

    // Trade type
    const val TRADE_BUY = "buy"
    const val TRADE_SELL = "sell"
}