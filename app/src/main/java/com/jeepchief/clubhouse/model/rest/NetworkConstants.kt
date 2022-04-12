package com.jeepchief.clubhouse.model.rest

object NetworkConstants {
    // Authorization key
    const val API_KET = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiMjA0Njk2NDQ4NCIsImF1dGhfaWQiOiIyIiwidG9rZW5fdHlwZSI6IkFjY2Vzc1Rva2VuIiwic2VydmljZV9pZCI6IjQzMDAxMTQ4MSIsIlgtQXBwLVJhdGUtTGltaXQiOiI1MDA6MTAiLCJuYmYiOjE2NDg1NjQwODQsImV4cCI6MTY2NDExNjA4NCwiaWF0IjoxNjQ4NTY0MDg0fQ.uyNQO0Qj7N1O6eUhAc__Z7SkYGCScKwVI8Crby8T-rA"

    // URL
    const val BASE_URL = "https://api.nexon.co.kr/fifaonline4/v1.0/"
    const val PLAYER_DATA = "https://static.api.nexon.co.kr/fifaonline4/latest/spid.json"
    const val MAX_DIVISION_URL = "https://api.nexon.co.kr/fifaonline4/v1.0/users/{accessid}/maxdivision"
    const val MATCH_TYPE_URL = "https://static.api.nexon.co.kr/fifaonline4/latest/matchtype.json"
}