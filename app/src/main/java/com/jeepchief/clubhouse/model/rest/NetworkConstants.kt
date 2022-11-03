package com.jeepchief.clubhouse.model.rest

object NetworkConstants {
    // Authorization key
    const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJYLUFwcC1SYXRlLUxpbWl0IjoiNTAwOjEwIiwiYWNjb3VudF9pZCI6IjIwNDY5NjQ0ODQiLCJhdXRoX2lkIjoiMiIsImV4cCI6MTY4MzAyNjE3MywiaWF0IjoxNjY3NDc0MTczLCJuYmYiOjE2Njc0NzQxNzMsInNlcnZpY2VfaWQiOiI0MzAwMTE0ODEiLCJ0b2tlbl90eXBlIjoiQWNjZXNzVG9rZW4ifQ.rnqDSUR8WG8BnOuW7uJ4PKNXubKx57aYUs2dK7TFJ3g"

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