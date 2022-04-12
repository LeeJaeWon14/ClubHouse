package com.jeepchief.clubhouse.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInfoDTO(
    @Expose
    @SerializedName("accessId")
    var accessId: String,
    @Expose
    @SerializedName("nickname")
    var nickname: String,
    @Expose
    @SerializedName("level")
    var level: Int
)